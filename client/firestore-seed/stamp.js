/*
 * The app only downloads a season again when `seasons/{id}.changedAt` moves,
 * and the players when `meta/players.changedAt` does. Whatever writes or
 * deletes documents calls this afterwards with the paths it touched.
 *
 * It goes after the data and in its own batch: a season file rewrites the
 * season document whole, and would wipe a stamp written before it.
 */

const admin = require('firebase-admin')

function targets(paths) {
  const stamps = new Set()

  paths.forEach((path) => {
    const parts = path.split('/')
    if (parts[0] === 'seasons' && parts[1]) stamps.add('seasons/' + parts[1])
    if (parts[0] === 'players') stamps.add('meta/players')
  })

  return [...stamps]
}

async function stampChanges(firestore, paths) {
  const stamps = targets(paths)
  if (!stamps.length) return

  const batch = firestore.batch()
  stamps.forEach((path) => {
    batch.set(
      firestore.doc(path),
      { changedAt: admin.firestore.FieldValue.serverTimestamp() },
      { merge: true }
    )
  })
  await batch.commit()

  stamps.forEach((path) => console.log('  stamped ' + path))
}

module.exports = { stampChanges }
