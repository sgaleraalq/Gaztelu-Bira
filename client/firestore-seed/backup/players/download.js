#!/usr/bin/env node
/*
 * Downloads a collection into one JSON file per document, in the same shape
 * upload.js writes back — so what comes down can go straight back up.
 *
 *   node download.js                                  # players -> backup/players
 *   node download.js --collection teams --out backup/teams
 *   node download.js --emulator
 *
 * Reading is harmless, so this one asks for nothing beyond the credentials.
 */

const fs = require('fs')
const admin = require('firebase-admin')

const DEFAULT_COLLECTION = 'players'
const DEFAULT_OUT = 'backup/players'

const args = process.argv.slice(2)
const has = (flag) => args.includes(flag)
const valueOf = (flag, fallback) => {
  const index = args.indexOf(flag)
  return index === -1 || index === args.length - 1 ? fallback : args[index + 1]
}

const collection = valueOf('--collection', DEFAULT_COLLECTION)
const out = valueOf('--out', collection === DEFAULT_COLLECTION ? DEFAULT_OUT : 'backup/' + collection)
const projectId = valueOf('--project', process.env.GCLOUD_PROJECT || 'gaztelu-bira')

main().catch((error) => {
  console.error(error.message || error)
  process.exit(1)
})

async function main() {
  const firestore = connect()
  const snapshot = await firestore.collection(collection).get()

  if (snapshot.empty) throw new Error('There is nothing in ' + collection)

  mkdirp(out)

  snapshot.docs.forEach((doc) => {
    const file = out + '/' + doc.id + '.json'
    fs.writeFileSync(file, JSON.stringify({
      path: collection + '/' + doc.id,
      data: plain(doc.data() || {})
    }, null, 2) + '\n')
    console.log('  ' + file)
  })

  console.log(snapshot.size + ' documents from ' + collection + ' into ' + out)
}

function mkdirp(directory) {
  if (fs.existsSync(directory)) return
  fs.mkdirSync(directory, { recursive: true })
}

/*
 * Dates come out as ISO strings: JSON has no timestamps, and upload.js turns
 * them back into the real thing on the way up.
 */
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
