'use strict'

/*
 * Decides whether a write on a match deserves a push, and builds it.
 *
 * Nothing here touches Firebase: it takes plain objects and returns plain objects, so it can be
 * tested with `node --test` and no project at all. index.js is the part that talks to Firebase.
 */

/*
 * What the function and the apps agree on. The same names live in
 * android/core/notifications (MatchNotificationContract.kt) and in
 * ios/GazteluBira/Notifications (MatchNotifications.swift): a change here has to be made in the
 * three places.
 */
const TOPIC = 'matches'
const ANDROID_CHANNEL_ID = 'matches'
const TYPE_MATCH_INSERTED = 'match_inserted'

/*
 * A match older than this is somebody loading history (upload.js, a restored backup), not news.
 * Without the limit, restoring a season would send one push per match to the whole team.
 */
const MAX_AGE_DAYS = 14
const DAY_IN_MILLIS = 24 * 60 * 60 * 1000

/**
 * Flattens a match document into what the push needs.
 *
 * The app writes teams and score under `match` and the date under `information`; the sample
 * season of firestore-seed keeps them at the root. Both are read, so the function does not care
 * which one wrote the document.
 *
 * @returns null when there is no document.
 */
function readMatch(data) {
  if (!data) return null

  const match = data.match || data
  const information = data.information || data

  return {
    competition: data.competition || {},
    date: toMillis(information.date),
    deleted: data.deletedAt != null,
    localTeam: match.localTeam || '',
    visitorTeam: match.visitorTeam || '',
    score: readScore(match.score)
  }
}

/** `score: null` is a match that has not been played, which is not the same as a 0-0. */
function readScore(score) {
  if (!score || !Number.isInteger(score.local) || !Number.isInteger(score.visitor)) return null

  return { local: score.local, visitor: score.visitor }
}

/** Firestore hands a Timestamp; the backups and the tests use dates and ISO strings. */
function toMillis(date) {
  if (date == null) return null
  if (typeof date.toMillis === 'function') return date.toMillis()

  const millis = new Date(date).getTime()
  return Number.isNaN(millis) ? null : millis
}

/**
 * A push goes out the first time a match has a result: either it is inserted already played,
 * which is what the insert screen does, or it was on the calendar with `score: null` and gets its
 * score now.
 *
 * It does not go out when the match is only scheduled, when it is deleted, when a result that was
 * already there is edited, or when the match is old (see MAX_AGE_DAYS).
 *
 * @param before the document before the write, undefined if it did not exist.
 * @param after the document after the write, undefined if it was deleted.
 * @param now milliseconds, so the tests choose the day.
 */
function shouldNotify(before, after, now) {
  const current = readMatch(after)
  if (!current || current.deleted || !current.score) return false

  const previous = readMatch(before)
  if (previous && previous.score) return false

  // No date means there is no way to tell it is old, and the app never writes a match without one.
  if (current.date == null) return true

  return now - current.date <= MAX_AGE_DAYS * DAY_IN_MILLIS
}

/** "Jornada 8", or the name and the round of the cup. */
function competitionLabel(competition) {
  const isCup = competition.type === 'CUP' || (!competition.type && competition.name)

  if (isCup) {
    return [competition.name, competition.round].filter(Boolean).join(' · ') || 'Copa'
  }

  return competition.journey ? `Jornada ${competition.journey}` : 'Liga'
}

/** For a team whose document cannot be read: `gruas_inaki_lima` becomes "Gruas Inaki Lima". */
function prettifyTeamId(teamId) {
  return teamId
    .split('_')
    .filter(Boolean)
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ')
}

/**
 * The message for FCM.
 *
 * The text travels in `notification`, so with the app closed both systems show it on their own
 * and no app has to compose it. `data` only carries what an app needs to open the match.
 *
 * The match id is also the tag on Android and the collapse id on iOS: Firestore may deliver the
 * same write twice, and with them the second push replaces the first instead of piling up.
 *
 * @param teamNames `{ [teamId]: name }`; a missing team falls back to its id, prettified.
 */
function buildMessage({ seasonId, matchId, match, teamNames = {} }) {
  const name = (teamId) => teamNames[teamId] || prettifyTeamId(teamId)

  return {
    topic: TOPIC,
    notification: {
      title: `${name(match.localTeam)} ${match.score.local} - ${match.score.visitor} ${name(match.visitorTeam)}`,
      body: `${competitionLabel(match.competition)} · Ya puedes ver el partido en la app`
    },
    data: {
      type: TYPE_MATCH_INSERTED,
      seasonId,
      matchId
    },
    android: {
      notification: {
        channelId: ANDROID_CHANNEL_ID,
        tag: matchId
      }
    },
    apns: {
      headers: { 'apns-collapse-id': matchId },
      payload: { aps: { sound: 'default' } }
    }
  }
}

module.exports = {
  MAX_AGE_DAYS,
  TOPIC,
  TYPE_MATCH_INSERTED,
  buildMessage,
  competitionLabel,
  readMatch,
  shouldNotify
}
