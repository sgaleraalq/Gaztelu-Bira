#!/usr/bin/env node
/*
 * Writes back a document exported with export-doc.js, replacing what is there.
 *
 *   node import-doc.js 2026_2027_l02.corregido.json --dry-run
 *   node import-doc.js 2026_2027_l02.corregido.json --emulator
 *   node import-doc.js 2026_2027_l02.corregido.json --production --yes
 *
 * "Replacing" means what it says: every document currently under the target
 * that the file does not bring is deleted. Export the document first if you
 * want a way back.
 */

const fs = require('fs')
const admin = require('firebase-admin')

const BATCH_SIZE = 400
const ISO_DATE = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d+)?(Z|[+-]\d{2}:\d{2})$/

const args = process.argv.slice(2)
const has = (flag) => args.includes(flag)
const valueOf = (flag, fallback) => {
  const index = args.indexOf(flag)
  return index === -1 || index === args.length - 1 ? fallback : args[index + 1]
}

const FLAGS_WITH_VALUE = ['--path', '--project']
const input = args.filter((arg, index) => {
  if (arg.startsWith('--')) return false
  return !(index > 0 && FLAGS_WITH_VALUE.includes(args[index - 1]))
})[0]

const dryRun = has('--dry-run')
const production = has('--production')
const keepTimestamps = has('--keep-timestamps')
const projectId = valueOf('--project', process.env.GCLOUD_PROJECT || 'gaztelu-bira')

main().catch((error) => {
  console.error(error.message || error)
  process.exit(1)
})

async function main() {
  if (!input) throw new Error('Which file? node import-doc.js 2026_2027_l02.corregido.json')

  const document = JSON.parse(fs.readFileSync(input, 'utf8'))
  const path = valueOf('--path', document.path)
  if (!path) throw new Error('The file carries no "path" and none was given with --path')

  const firestore = connect()
  const reference = firestore.doc(path)

  const incoming = plan(path, document)
  const existing = dryRun && !has('--emulator') && !production ? [] : await currentPaths(reference)
  const removed = existing.filter((each) => incoming.every((write) => write.path !== each))

  console.log(path)
  console.log('  ' + incoming.length + ' documents to write')
  removed.forEach((each) => console.log('  - delete ' + each))

  if (dryRun) {
    console.log(JSON.stringify(incoming, null, 2))
    console.log('nothing written (--dry-run)')
    return
  }

  await commit(firestore, incoming, removed)
  console.log('done')
}

/*
 * The document itself plus one entry per subcollection document, in the order
 * they will be written.
 */
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

/** Everything that hangs off the document right now, so we know what to remove. */
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
 * would read a String where it expects a date. `updatedAt` is left to the
 * server, which is the only clock every device agrees on.
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
    if (!has('--yes')) {
      throw new Error(
        'This rewrites documents in the real project ' + projectId + ', deleting what the file does not bring.\n' +
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

if (typeof module !== 'undefined') module.exports = { plan: plan, revive: revive }
