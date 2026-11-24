#!/usr/bin/env node
/*
 * Downloads the teams and the squad of one season into one JSON file per
 * document, in the same shape upload.js writes back:
 *
 *   seasons/2026_2027/teams/aterbea -> backup/seasons/2026_2027/teams/aterbea.json
 *
 *   node download-season.js 2026_2027
 *   node download-season.js 2026_2027 --only teams
 *   node download-season.js 2026_2027 --emulator
 *
 * Firestore wins: a file that differs is overwritten. A file whose document no
 * longer exists is reported and left alone, because upload.js would push it
 * back up — deleting it is your call.
 */

const fs = require('fs')
const path = require('path')
const admin = require('firebase-admin')

const COLLECTIONS = ['teams', 'squad']
const FLAGS_WITH_VALUE = ['--only', '--project']

const args = process.argv.slice(2)
const has = (flag) => args.includes(flag)
const valueOf = (flag, fallback) => {
  const index = args.indexOf(flag)
  return index === -1 || index === args.length - 1 ? fallback : args[index + 1]
}

const seasonId = args.filter((arg, index) => {
  if (arg.startsWith('--')) return false
  return !(index > 0 && FLAGS_WITH_VALUE.includes(args[index - 1]))
})[0]

const only = valueOf('--only', null)
const projectId = valueOf('--project', process.env.GCLOUD_PROJECT || 'gaztelu-bira')

main().catch((error) => {
  console.error(error.message || error)
  process.exit(1)
})

async function main() {
  if (!seasonId) throw new Error('Which season? node download-season.js 2026_2027')
  if (only && !COLLECTIONS.includes(only)) {
    throw new Error('--only takes one of: ' + COLLECTIONS.join(', '))
  }

  const firestore = connect()
  const season = await firestore.collection('seasons').doc(seasonId).get()
  if (!season.exists) throw new Error('There is no season ' + seasonId)

  for (const collection of only ? [only] : COLLECTIONS) {
    await download(firestore, collection)
  }
}

async function download(firestore, collection) {
  const remote = 'seasons/' + seasonId + '/' + collection
  const out = path.join(__dirname, seasonId, collection)
  const snapshot = await firestore.collection(remote).get()

  console.log('\n' + remote)

  if (snapshot.empty) {
    console.log('  nothing there')
    return
  }

  mkdirp(out)

  const counts = { new: 0, updated: 0, same: 0 }
  const downloaded = new Set()

  snapshot.docs.forEach((doc) => {
    const file = path.join(out, doc.id + '.json')
    const content = JSON.stringify({
      path: remote + '/' + doc.id,
      data: plain(doc.data() || {})
    }, null, 2) + '\n'

    const state = !fs.existsSync(file)
      ? 'new'
      : fs.readFileSync(file, 'utf8') === content ? 'same' : 'updated'

    if (state !== 'same') {
      fs.writeFileSync(file, content)
      console.log('  ' + state.padEnd(8) + doc.id)
    }

    counts[state]++
    downloaded.add(doc.id + '.json')
  })

  fs.readdirSync(out)
    .filter((file) => file.endsWith('.json') && !downloaded.has(file))
    .forEach((file) => console.log('  only local, not in Firestore: ' + file))

  console.log(
    '  ' + snapshot.size + ' documents: ' +
    counts.new + ' new, ' + counts.updated + ' updated, ' + counts.same + ' unchanged'
  )
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
