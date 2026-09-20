#!/usr/bin/env node
/*
 * Writes the sample season from data.js into Firestore.
 *
 * It talks to the local emulator unless you ask for a real project out loud:
 * a seed that can silently land on production is a seed nobody dares to run.
 */

const admin = require('firebase-admin')
const { SEASON_ID, buildDocuments, verify } = require('./data')

const BATCH_SIZE = 400
const DEFAULT_EMULATOR = '127.0.0.1:8080'

const args = process.argv.slice(2)
const has = (flag) => args.includes(flag)
const valueOf = (flag, fallback) => {
  const index = args.indexOf(flag)
  return index === -1 || index === args.length - 1 ? fallback : args[index + 1]
}

const dryRun = has('--dry-run')
const production = has('--production')
const clean = has('--clean')
const projectId = valueOf('--project', process.env.GCLOUD_PROJECT || 'gbmultiplatform')

main().catch((error) => {
  console.error(error.message || error)
  process.exit(1)
})

async function main() {
  const problems = verify()
  if (problems.length) {
    console.error('The sample data contradicts itself, nothing was written:')
    problems.forEach((problem) => console.error('  - ' + problem))
    process.exit(1)
  }

  const documents = buildDocuments()

  if (dryRun) {
    documents.forEach((doc) => {
      console.log(doc.path)
      console.log(JSON.stringify(doc.data, null, 2))
      console.log('')
    })
    console.log(documents.length + ' documents, nothing written (--dry-run)')
    return
  }

  const firestore = connect()

  if (clean) {
    console.log('Removing the previous ' + SEASON_ID + '...')
    await firestore.recursiveDelete(firestore.doc('seasons/' + SEASON_ID))
    const players = documents.filter((doc) => doc.path.startsWith('players/'))
    await commit(firestore, players.map((doc) => ({ path: doc.path, delete: true })))
  }

  await commit(firestore, documents)
  console.log(documents.length + ' documents written under seasons/' + SEASON_ID)
}

function connect() {
  if (production) {
    if (!has('--yes')) {
      throw new Error(
        'Refusing to write to the real project ' + projectId + '.\n' +
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
    process.env.FIRESTORE_EMULATOR_HOST = process.env.FIRESTORE_EMULATOR_HOST || DEFAULT_EMULATOR
    console.log('Writing to the emulator at ' + process.env.FIRESTORE_EMULATOR_HOST)
    admin.initializeApp({ projectId: projectId })
  }

  return admin.firestore()
}

/*
 * Firestore takes 500 writes per batch, and the server clock is the one that
 * decides `updatedAt`: the phone that inserts a match may be minutes off, and
 * every other device would then miss the change.
 */
async function commit(firestore, operations) {
  for (let start = 0; start < operations.length; start += BATCH_SIZE) {
    const batch = firestore.batch()

    operations.slice(start, start + BATCH_SIZE).forEach((operation) => {
      const ref = firestore.doc(operation.path)
      if (operation.delete) {
        batch.delete(ref)
      } else {
        batch.set(ref, Object.assign({}, operation.data, {
          updatedAt: admin.firestore.FieldValue.serverTimestamp()
        }))
      }
    })

    await batch.commit()
  }
}
