#!/usr/bin/env node
/*
 * Uploads the documents kept under `backup/` and replaces what Firestore has.
 *
 * Each file carries the path it belongs to, so that folder is a mirror of the
 * database that you can edit by hand and push back:
 *
 *   backup/seasons/2026_2027/2026_2027_l02.json -> seasons/2026_2027/matches/2026_2027_l02
 *   backup/players/pedro_garces.json            -> players/pedro_garces
 *
 *   node upload.js --dry-run                    # says what it would do
 *   node upload.js --emulator                   # against the local emulator
 *   node upload.js --production --yes           # for real
 *   node upload.js backup/players --production --yes
 *   node upload.js --only-missing --production --yes
 *
 * Replacing means deleting: any document under a target that its file does not
 * bring is removed, so the result matches the file exactly. Unless you pass
 * --only-missing, which adds what Firestore does not have and touches nothing
 * else.
 */

const fs = require('fs')
const admin = require('firebase-admin')

const BATCH_SIZE = 400
const DEFAULT_INPUT = 'backup'
const ISO_DATE = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d+)?(Z|[+-]\d{2}:\d{2})$/
const FLAGS_WITH_VALUE = ['--path', '--project']

const args = process.argv.slice(2)
const has = (flag) => args.includes(flag)
const valueOf = (flag, fallback) => {
  const index = args.indexOf(flag)
  return index === -1 || index === args.length - 1 ? fallback : args[index + 1]
}

const input = args.filter((arg, index) => {
  if (arg.startsWith('--')) return false
  return !(index > 0 && FLAGS_WITH_VALUE.includes(args[index - 1]))
})[0] || DEFAULT_INPUT

const dryRun = has('--dry-run')
const onlyMissing = has('--only-missing')
const production = has('--production')
const keepTimestamps = has('--keep-timestamps')
const projectId = valueOf('--project', process.env.GCLOUD_PROJECT || 'gaztelu-bira')

main().catch((error) => {
  console.error(error.message || error)
  process.exit(1)
})

async function main() {
  const files = jsonFiles(input)
  if (!files.length) throw new Error('No .json documents found in ' + input)

  const firestore = connect()

  for (const file of files) {
    const document = JSON.parse(fs.readFileSync(file, 'utf8'))
    const path = valueOf('--path', document.path)
    if (!path) throw new Error(file + ' carries no "path" and none was given with --path')

    const incoming = plan(path, document)
    const writes = onlyMissing ? await missing(firestore, incoming) : incoming
    const removed = onlyMissing
      ? []
      : (await currentPaths(firestore.doc(path))).filter((each) => incoming.every((write) => write.path !== each))

    console.log(file + '  ->  ' + path)
    console.log(onlyMissing
      ? '  ' + writes.length + ' new, ' + (incoming.length - writes.length) + ' already there'
      : '  ' + writes.length + ' documents to write')
    removed.forEach((each) => console.log('  - delete ' + each))

    if (!dryRun && writes.length + removed.length) await commit(firestore, writes, removed)
  }

  console.log(dryRun ? 'nothing written (--dry-run)' : 'done')
}

/**
 * A single file, or every .json under a folder — which is what lets the local
 * tree grow match by match without touching this script again.
 */
function jsonFiles(target) {
  if (!fs.existsSync(target)) throw new Error('There is no ' + target)
  if (!fs.statSync(target).isDirectory()) return [target]

  return fs.readdirSync(target)
    .map((name) => target + '/' + name)
    .reduce((files, each) => files.concat(jsonFiles(each)), [])
    .filter((name) => name.endsWith('.json'))
    .sort()
}

/** The document itself plus one entry per subcollection document. */
function plan(path, document) {
  const writes = [{ path: path, data: document.data || {} }]
  const subcollections = document.subcollections || {}

  Object.keys(subcollections).forEach((name) => {
    Object.keys(subcollections[name]).forEach((id) => {
      writes.push({ path: path + '/' + name + '/' + id, data: subcollections[name][id] })
    })
  })

  return writes
}

/*
 * The ones Firestore does not have yet. Costs one read per document, which is
 * the price of never overwriting something someone else already edited.
 */
async function missing(firestore, writes) {
  const absent = []

  for (const write of writes) {
    const snapshot = await firestore.doc(write.path).get()
    if (!snapshot.exists) absent.push(write)
  }

  return absent
}

/** Everything hanging off the document right now, so we know what to remove. */
async function currentPaths(reference) {
  const paths = []
  const collections = await reference.listCollections()

  for (const collection of collections) {
    const snapshot = await collection.get()
    snapshot.docs.forEach((doc) => paths.push(doc.ref.path))
  }

  return paths
}

/*
 * JSON has no dates: an ISO string goes back in as a Timestamp, or the app
 * would read a String where it expects a date — and would not notice until
 * something tried to sort by it. `updatedAt` is left to the server, the only
 * clock every device agrees on.
 */
function revive(value, key) {
  if (value === null || value === undefined) return null
  if (Array.isArray(value)) return value.map((item) => revive(item))

  if (typeof value === 'object') {
    const copy = {}
    Object.keys(value).forEach((name) => { copy[name] = revive(value[name], name) })
    return copy
  }

  if (key === 'updatedAt' && !keepTimestamps) return admin.firestore.FieldValue.serverTimestamp()
  if (typeof value === 'string' && ISO_DATE.test(value)) {
    return admin.firestore.Timestamp.fromDate(new Date(value))
  }

  return value
}

/** Deletes and writes travel together: either the document ends up as the file says, or nothing moves. */
async function commit(firestore, writes, removed) {
  const operations = removed.map((path) => ({ path: path, delete: true })).concat(writes)

  for (let start = 0; start < operations.length; start += BATCH_SIZE) {
    const batch = firestore.batch()

    operations.slice(start, start + BATCH_SIZE).forEach((operation) => {
      const reference = firestore.doc(operation.path)
      if (operation.delete) batch.delete(reference)
      else batch.set(reference, revive(operation.data))
    })

    await batch.commit()
  }
}

function connect() {
  if (production) {
    // a dry run writes nothing, so it does not need the blessing
    if (!has('--yes') && !dryRun) {
      throw new Error(
        (onlyMissing
          ? 'This writes the documents missing from the real project ' + projectId + '.\n'
          : 'This rewrites documents in the real project ' + projectId + ', deleting what the files do not bring.\n') +
        'Add --yes once you are sure, or drop --production to use the emulator.'
      )
    }
    if (!process.env.GOOGLE_APPLICATION_CREDENTIALS) {
      throw new Error('GOOGLE_APPLICATION_CREDENTIALS must point to a service account key.')
    }
    console.log('Writing to the REAL project: ' + projectId)
    admin.initializeApp({
      credential: admin.credential.applicationDefault(),
      projectId: projectId
    })
  } else {
    process.env.FIRESTORE_EMULATOR_HOST = process.env.FIRESTORE_EMULATOR_HOST || '127.0.0.1:8080'
    console.log('Writing to the emulator at ' + process.env.FIRESTORE_EMULATOR_HOST)
    admin.initializeApp({ projectId: projectId })
  }

  return admin.firestore()
}
