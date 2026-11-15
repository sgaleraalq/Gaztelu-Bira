'use strict'

const assert = require('node:assert/strict')
const { test } = require('node:test')

const {
  MAX_AGE_DAYS,
  buildMessage,
  competitionLabel,
  readMatch,
  shouldNotify
} = require('./match-notification')

const NOW = Date.parse('2026-11-15T12:00:00.000Z')
const YESTERDAY = '2026-11-14T09:30:00.000Z'

/** A match the way the app writes it. */
function appMatch({ score = { local: 2, visitor: 1 }, date = YESTERDAY, deletedAt } = {}) {
  const match = {
    competition: { type: 'LEAGUE', journey: 8 },
    match: { localTeam: 'gaztelu_bira', visitorTeam: 'ecovaldi', formation: '4-3-3', score },
    information: { date, location: 'Amigó', description: '' }
  }
  if (deletedAt !== undefined) match.deletedAt = deletedAt

  return match
}

test('a match inserted with its result sends a push', () => {
  assert.equal(shouldNotify(undefined, appMatch(), NOW), true)
})

test('a scheduled match does not, until it gets its score', () => {
  const scheduled = appMatch({ score: null })

  assert.equal(shouldNotify(undefined, scheduled, NOW), false)
  assert.equal(shouldNotify(scheduled, appMatch(), NOW), true)
})

test('a 0-0 is a result, not a match without score', () => {
  assert.equal(shouldNotify(undefined, appMatch({ score: { local: 0, visitor: 0 } }), NOW), true)
})

test('editing a match that already had a result does not send another push', () => {
  const corrected = appMatch({ score: { local: 3, visitor: 1 } })

  assert.equal(shouldNotify(appMatch(), corrected, NOW), false)
})

test('deleting a match sends nothing, whether it is removed or marked', () => {
  assert.equal(shouldNotify(appMatch(), undefined, NOW), false)
  assert.equal(shouldNotify(appMatch(), appMatch({ deletedAt: YESTERDAY }), NOW), false)
  assert.equal(shouldNotify(undefined, appMatch({ deletedAt: YESTERDAY }), NOW), false)
})

test('an explicit deletedAt null is a match that is alive', () => {
  assert.equal(shouldNotify(undefined, appMatch({ deletedAt: null }), NOW), true)
})

test('loading old matches does not notify anybody', () => {
  const day = 24 * 60 * 60 * 1000
  const justInside = new Date(NOW - MAX_AGE_DAYS * day).toISOString()
  const justOutside = new Date(NOW - MAX_AGE_DAYS * day - 1).toISOString()

  assert.equal(shouldNotify(undefined, appMatch({ date: justInside }), NOW), true)
  assert.equal(shouldNotify(undefined, appMatch({ date: justOutside }), NOW), false)
  assert.equal(shouldNotify(undefined, appMatch({ date: '2024-10-06T09:30:00.000Z' }), NOW), false)
})

test('the date is understood as a Firestore Timestamp too', () => {
  const timestamp = { toMillis: () => Date.parse(YESTERDAY) }

  assert.equal(readMatch(appMatch({ date: timestamp })).date, Date.parse(YESTERDAY))
})

test('the flat layout of the sample season is read like the one of the app', () => {
  const flat = {
    competition: { type: 'LEAGUE', journey: 5 },
    date: YESTERDAY,
    localTeam: 'lakua',
    visitorTeam: 'gaztelu_bira',
    score: { local: 0, visitor: 2 },
    deletedAt: null
  }

  assert.deepEqual(readMatch(flat), {
    competition: { type: 'LEAGUE', journey: 5 },
    date: Date.parse(YESTERDAY),
    deleted: false,
    localTeam: 'lakua',
    visitorTeam: 'gaztelu_bira',
    score: { local: 0, visitor: 2 }
  })
  assert.equal(shouldNotify(undefined, flat, NOW), true)
})

test('the competition reads as the journey, or as the cup and its round', () => {
  assert.equal(competitionLabel({ type: 'LEAGUE', journey: 8 }), 'Jornada 8')
  assert.equal(competitionLabel({ type: 'LEAGUE' }), 'Liga')
  assert.equal(
    competitionLabel({ type: 'CUP', name: 'Copa Gasteiz', round: 'Cuartos' }),
    'Copa Gasteiz · Cuartos'
  )
  assert.equal(competitionLabel({ type: 'CUP' }), 'Copa')
  // Old documents have no type: a name is what tells a cup apart.
  assert.equal(competitionLabel({ name: 'Copa Cocinobra' }), 'Copa Cocinobra')
})

test('the message carries the text for the system and the ids for the app', () => {
  const message = buildMessage({
    seasonId: '2026_2027',
    matchId: '2026_2027_l08',
    match: readMatch(appMatch()),
    teamNames: { gaztelu_bira: 'Gaztelu Bira', ecovaldi: 'Ecovaldi' }
  })

  assert.deepEqual(message, {
    topic: 'matches',
    notification: {
      title: 'Gaztelu Bira 2 - 1 Ecovaldi',
      body: 'Jornada 8 · Ya puedes ver el partido en la app'
    },
    data: { type: 'match_inserted', seasonId: '2026_2027', matchId: '2026_2027_l08' },
    android: { notification: { channelId: 'matches', tag: '2026_2027_l08' } },
    apns: {
      headers: { 'apns-collapse-id': '2026_2027_l08' },
      payload: { aps: { sound: 'default' } }
    }
  })
})

test('a team without a readable name is shown by its id', () => {
  const match = readMatch(appMatch())
  match.visitorTeam = 'gruas_transporte_inaki_lima'

  const message = buildMessage({
    seasonId: '2026_2027',
    matchId: '2026_2027_l08',
    match,
    teamNames: { gaztelu_bira: 'Gaztelu Bira' }
  })

  assert.equal(message.notification.title, 'Gaztelu Bira 2 - 1 Gruas Transporte Inaki Lima')
})
