#!/usr/bin/env node
/*
 * Dumps one document — and everything hanging off it — into a JSON file you can
 * read, paste or send. Made for reviewing a shape, not for restoring it: dates
 * come out as ISO strings rather than tagged objects.
 *
 *   node export-doc.js seasons/2026_2027/matches/2026_2027_l02
 *   node export-doc.js <path> --out partido.json
 *   node export-doc.js <path> --emulator
 */

const fs = require('fs')
const admin = require('firebase-admin')

const FLAGS_WITH_VALUE = ['--out', '--project']

const args = process.argv.slice(2)
const has = (flag) => args.includes(flag)
const valueOf = (flag, fallback) => {
  const index = args.indexOf(flag)
  return index === -1 || index === args.length - 1 ? fallback : args[index + 1]
}

const path = args.filter((arg, index) => {
  if (arg.startsWith('--')) return false
  return !(index > 0 && FLAGS_WITH_VALUE.includes(args[index - 1]))
})[0]

const projectId = valueOf('--project', process.env.GCLOUD_PROJECT || 'gaztelu-bira')
const out = valueOf('--out', path ? path.split('/').pop() + '.json' : 'document.json')

main().catch((error) => {
  console.error(error.message || error)
  process.exit(1)
})

async function main() {
  if (!path) throw new Error('Which document? node export-doc.js seasons/2026_2027/matches/2026_2027_l02')
  if (path.split('/').length % 2 !== 0) {
    throw new Error('"' + path + '" points at a collection, not a document: a document path has an even number of segments.')
  }

  const firestore = connect()
  const document = await read(firestore.doc(path))

  fs.writeFileSync(out, JSON.stringify(document, null, 2))
  console.log(count(document) + ' documents written to ' + out)
}

/*
 * A document is never alone: whatever hangs below it is part of the shape being
 * reviewed, so the subcollections come along.
 */
async function read(reference) {
  const snapshot = await reference.get()
  if (!snapshot.exists) throw new Error('Nothing at ' + reference.path)

  const node = { path: reference.path, data: plain(snapshot.data() || {}) }
  const children = await reference.listCollections()

  for (const collection of children) {
    node.subcollections = node.subcollections || {}
    const documents = await collection.get()

    node.subcollections[collection.id] = {}
    for (const child of documents.docs) {
      node.subcollections[collection.id][child.id] = (await read(child.ref)).data
    }
  }

  return node
}

function plain(value) {
  if (value === null || value === undefined) return null
  if (Array.isArray(value)) return value.map(plain)

  if (typeof value === 'object') {
    if (typeof value.toDate === 'function') return value.toDate().toISOString()
    if (typeof value.latitude === 'number' && typeof value.longitude === 'number') {
      return { latitude: value.latitude, longitude: value.longitude }
    }
    if (typeof value.path === 'string' && typeof value.collection === 'function') {
      return '→ ' + value.path
    }

    const copy = {}
    Object.keys(value).forEach((key) => { copy[key] = plain(value[key]) })
    return copy
  }

  return value
}

function count(node) {
  let total = 1
  Object.keys(node.subcollections || {}).forEach((name) => {
    total += Object.keys(node.subcollections[name]).length
  })
  return total
}

function connect() {
  if (has('--emulator')) {
    process.env.FIRESTORE_EMULATOR_HOST = process.env.FIRESTORE_EMULATOR_HOST || '127.0.0.1:8080'
    console.log('Reading the emulator at ' + process.env.FIRESTORE_EMULATOR_HOST)
    admin.initializeApp({ projectId: projectId })
  } else {
    if (!process.env.GOOGLE_APPLICATION_CREDENTIALS) {
      throw new Error(
        'GOOGLE_APPLICATION_CREDENTIALS must point to a service account key,\n' +
        'or pass --emulator to read the local emulator.'
      )
    }
    console.log('Reading project ' + projectId)
    admin.initializeApp({
      credential: admin.credential.applicationDefault(),
      projectId: projectId
    })
  }

  return admin.firestore()
}
