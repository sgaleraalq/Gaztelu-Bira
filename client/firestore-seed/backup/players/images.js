#!/usr/bin/env node
/*
 * Walks Storage under `players/` and writes each photo's URL into the player
 * JSON that sits next to this script, so nobody has to paste 31 links by hand.
 *
 *   node backup/players/images.js --dry-run     # shows the pairing, writes nothing
 *   node backup/players/images.js               # updates the JSON files
 *
 * The layout in Storage is not assumed: every file is matched to a player by
 * its path, and whatever cannot be matched is listed instead of guessed.
 */

const fs = require('fs')
const admin = require('firebase-admin')

const PREFIX = 'players/'
const PLAYERS_DIR = __dirname
const DEFAULT_BUCKET = 'gaztelu-bira.firebasestorage.app'

const args = process.argv.slice(2)
const has = (flag) => args.includes(flag)
const valueOf = (flag, fallback) => {
  const index = args.indexOf(flag)
  return index === -1 || index === args.length - 1 ? fallback : args[index + 1]
}

const dryRun = has('--dry-run')
const bucketName = valueOf('--bucket', process.env.FIREBASE_STORAGE_BUCKET || DEFAULT_BUCKET)
const projectId = valueOf('--project', process.env.GCLOUD_PROJECT || 'gaztelu-bira')

main().catch((error) => {
  console.error(error.message || error)
  process.exit(1)
})

async function main() {
  if (!process.env.GOOGLE_APPLICATION_CREDENTIALS) {
    throw new Error('GOOGLE_APPLICATION_CREDENTIALS must point to a service account key.')
  }

  admin.initializeApp({
    credential: admin.credential.applicationDefault(),
    projectId: projectId,
    storageBucket: bucketName
  })

  const [files] = await admin.storage().bucket(bucketName).getFiles({ prefix: PREFIX })
  const photos = {}
  const puzzling = []

  files.forEach((file) => {
    if (file.name.endsWith('/')) return // folder marker

    const match = classify(file.name)
    if (!match) {
      puzzling.push(file.name)
      return
    }

    photos[match.id] = photos[match.id] || {}
    photos[match.id][match.field] = urlOf(file)
    if (match.assumed) console.log('  assumed face: ' + file.name)
  })

  const changed = []
  players().forEach((entry) => {
    const found = photos[entry.id]
    if (!found) {
      console.log('  no photo: ' + entry.id)
      return
    }

    const before = JSON.stringify(entry.document.data)
    Object.keys(found).forEach((field) => { entry.document.data[field] = found[field] })
    if (JSON.stringify(entry.document.data) === before) return

    changed.push(entry.id)
    if (!dryRun) {
      fs.writeFileSync(entry.file, JSON.stringify(entry.document, null, 2) + '\n')
    }
  })

  Object.keys(photos).filter((id) => !players().some((entry) => entry.id === id))
    .forEach((id) => puzzling.push('photo for unknown player: ' + id))

  console.log(files.length + ' files in ' + PREFIX + ', ' + changed.length + ' players updated')
  changed.forEach((id) => console.log('  ' + id))
  puzzling.forEach((each) => console.log('  ? ' + each))
  if (dryRun) console.log('nothing written (--dry-run)')
}

/*
 * A file belongs to a player and is either their face or their body. Both
 * layouts show up in practice — a folder per player, or the id in the file
 * name — so both are read, and a photo with no hint is taken as the face,
 * which is the one everybody uploads first.
 */
function classify(path) {
  const parts = path.slice(PREFIX.length).split('/').filter(Boolean)
  if (!parts.length) return null

  const name = parts[parts.length - 1]
  const base = name.replace(/\.[^.]+$/, '')
  const hint = parts.join('/').toLowerCase()

  const isBody = /body|cuerpo/.test(hint)
  const isFace = /face|cara/.test(hint)

  const id = parts.length > 1
    ? parts[0]
    : base.replace(/[_-]?(face|body|cara|cuerpo)$/i, '')

  if (!id) return null

  return {
    id: id,
    field: isBody ? 'bodyImage' : 'faceImage',
    assumed: !isBody && !isFace
  }
}

/** The download URL the app already uses, token included when the file has one. */
function urlOf(file) {
  const token = (file.metadata && file.metadata.metadata || {}).firebaseStorageDownloadTokens
  return 'https://firebasestorage.googleapis.com/v0/b/' + bucketName +
    '/o/' + encodeURIComponent(file.name) + '?alt=media' + (token ? '&token=' + token.split(',')[0] : '')
}

function players() {
  return fs.readdirSync(PLAYERS_DIR)
    .filter((name) => name.endsWith('.json'))
    .map((name) => {
      const file = PLAYERS_DIR + '/' + name
      const document = JSON.parse(fs.readFileSync(file, 'utf8'))
      return { id: name.replace(/\.json$/, ''), file: file, document: document }
    })
}
