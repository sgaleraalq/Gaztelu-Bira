'use strict'

/*
 * The only thing that sends pushes. The apps never do: they write the match, as always, and this
 * function finds out by watching Firestore. That is what keeps the notifications away from every
 * insert screen — whoever writes the document (Android, iOS, a script) gets the push for free.
 */

const { onDocumentWritten } = require('firebase-functions/v2/firestore')
const logger = require('firebase-functions/logger')
const { initializeApp } = require('firebase-admin/app')
const { getFirestore } = require('firebase-admin/firestore')
const { getMessaging } = require('firebase-admin/messaging')

const { buildMessage, readMatch, shouldNotify } = require('./match-notification')

/*
 * Where the function runs. The trigger itself always lives next to the database, wherever that
 * is; this is only the machine that wakes up, so the closest region to the database is the best.
 */
const REGION = 'europe-west1'

initializeApp()

exports.notifyMatchInserted = onDocumentWritten(
  {
    document: 'seasons/{seasonId}/matches/{matchId}',
    region: REGION,
    // A push that failed is not worth sending hours later, and a retry of one that did go out
    // would notify twice.
    retry: false
  },
  async (event) => {
    const before = event.data && event.data.before.data()
    const after = event.data && event.data.after.data()
    const { seasonId, matchId } = event.params

    if (!shouldNotify(before, after, Date.now())) {
      logger.debug('No push for this write', { seasonId, matchId })
      return
    }

    const match = readMatch(after)
    const teamNames = await fetchTeamNames(seasonId, [match.localTeam, match.visitorTeam])
    const message = buildMessage({ seasonId, matchId, match, teamNames })

    const messageId = await getMessaging().send(message)
    logger.info('Push sent', { seasonId, matchId, messageId, title: message.notification.title })
  }
)

/**
 * The match only stores the ids of its teams; the names are in `seasons/{season}/teams`.
 *
 * A team that cannot be read does not stop the push: buildMessage falls back to its id.
 *
 * @returns `{ [teamId]: name }` with the teams that were found.
 */
async function fetchTeamNames(seasonId, teamIds) {
  const teams = getFirestore().collection('seasons').doc(seasonId).collection('teams')
  const names = {}

  await Promise.all(
    teamIds.filter(Boolean).map(async (teamId) => {
      try {
        const team = await teams.doc(teamId).get()
        const name = team.exists && team.data().name
        if (name) names[teamId] = name
      } catch (error) {
        logger.warn('Could not read the team, its id will be shown', { seasonId, teamId, error })
      }
    })
  )

  return names
}
