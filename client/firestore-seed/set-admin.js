#!/usr/bin/env node
/*
 * Grants the `admin` custom claim: the one the Firestore rules check before any
 * write, and the one the app reads to decide whether to offer the admin screens.
 *
 * A claim lives on the account in Firebase Auth, not in Firestore, and the
 * emulator here only runs Firestore: this always talks to the real project, so
 * it asks for --yes.
 *
 *   node set-admin.js someone@example.com --yes                  # gaztelu-bira
 *   node set-admin.js someone@example.com --project X --yes
 *   node set-admin.js someone@example.com --revoke --yes
 *
 * The app refreshes the token every time it starts, so the change shows on the
 * next launch.
 */

const admin = require('firebase-admin')

const FLAGS_WITH_VALUE = ['--project']

const args = process.argv.slice(2)
const has = (flag) => args.includes(flag)
const valueOf = (flag, fallback) => {
  const index = args.indexOf(flag)
  return index === -1 || index === args.length - 1 ? fallback : args[index + 1]
}

const email = args.filter((arg, index) => {
  if (arg.startsWith('--')) return false
  return !(index > 0 && FLAGS_WITH_VALUE.includes(args[index - 1]))
})[0]

const revoke = has('--revoke')
const projectId = valueOf('--project', process.env.GCLOUD_PROJECT || 'gaztelu-bira')

main().catch((error) => {
  console.error(error.message || error)
  process.exit(1)
})

async function main() {
  if (!email) {
    throw new Error('Usage: node set-admin.js <email> [--revoke] [--project X] --yes')
  }
  if (!has('--yes')) {
    throw new Error(
      (revoke ? 'This takes admin away from ' : 'This makes an admin of ') + email +
      ' in the real project ' + projectId + '.\n' +
      'Add --yes once you are sure.'
    )
  }
  if (!process.env.GOOGLE_APPLICATION_CREDENTIALS) {
    throw new Error('GOOGLE_APPLICATION_CREDENTIALS must point to a service account key.')
  }

  admin.initializeApp({
    credential: admin.credential.applicationDefault(),
    projectId: projectId
  })

  const user = await admin.auth().getUserByEmail(email)

  // any other claim the account carries stays as it was
  const claims = { ...(user.customClaims || {}) }
  if (revoke) delete claims.admin
  else claims.admin = true

  await admin.auth().setCustomUserClaims(user.uid, claims)
  console.log(
    (revoke ? 'Admin taken away from ' : 'Admin granted to ') +
    email + ' (' + user.uid + ') in ' + projectId
  )
}
