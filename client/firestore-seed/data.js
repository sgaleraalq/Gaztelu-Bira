/*
 * Sample season for the Firestore layout: seasons/{season}/{teams,squad,matches/{match}/lineup}.
 *
 * Everything here is pure data and pure functions, so it can be inspected and
 * checked without touching Firebase at all. `seed.js` is what writes it.
 */

var SEASON_ID = '2026_2027'
var APP_TEAM = 'gaztelu_bira'

var SEASON = {
  name: 'Temporada 2026-2027',
  startsAt: new Date('2026-08-01T00:00:00Z'),
  endsAt: new Date('2027-06-30T00:00:00Z'),
  isCurrent: true
}

var TEAMS = [
  { id: APP_TEAM, name: 'Gaztelu Bira', logo: null },
  { id: 'lakua', name: 'C.D. Lakua', logo: null },
  { id: 'zaramaga', name: 'Zaramaga F.C.', logo: null },
  { id: 'abetxuko', name: 'Abetxuko Kirol Taldea', logo: null },
  { id: 'judimendi', name: 'Judimendi C.F.', logo: null },
  { id: 'salburua', name: 'Salburua Sport', logo: null }
]

/*
 * The person and the shirt are two different things: `players/{id}` is who they
 * are, the squad entry is what they were this season. Next season the same
 * person can change dorsal or position without becoming a new player.
 */
var PLAYERS = [
  { id: 'iker_mendiola',    name: 'Iker Mendiola',    dorsal: 1,    position: 'GOALKEEPER' },
  { id: 'unai_larrea',      name: 'Unai Larrea',      dorsal: 2,    position: 'DEFENDER' },
  { id: 'mikel_otxoa',      name: 'Mikel Otxoa',      dorsal: 3,    position: 'DEFENDER' },
  { id: 'jon_zabala',       name: 'Jon Zabala',       dorsal: 4,    position: 'DEFENDER' },
  { id: 'benat_uriarte',    name: 'Beñat Uriarte',    dorsal: 5,    position: 'DEFENDER' },
  { id: 'oier_agirre',      name: 'Oier Agirre',      dorsal: 6,    position: 'DEFENDER' },
  { id: 'ander_etxeberria', name: 'Ander Etxeberria', dorsal: 7,    position: 'MIDFIELDER' },
  { id: 'julen_arrieta',    name: 'Julen Arrieta',    dorsal: 8,    position: 'MIDFIELDER' },
  { id: 'eneko_garmendia',  name: 'Eneko Garmendia',  dorsal: 9,    position: 'FORWARD' },
  { id: 'xabi_lasa',        name: 'Xabi Lasa',        dorsal: 10,   position: 'MIDFIELDER' },
  { id: 'gorka_ibarra',     name: 'Gorka Ibarra',     dorsal: 11,   position: 'FORWARD' },
  { id: 'asier_goitia',     name: 'Asier Goitia',     dorsal: 13,   position: 'GOALKEEPER' },
  { id: 'markel_bilbao',    name: 'Markel Bilbao',    dorsal: 14,   position: 'MIDFIELDER' },
  { id: 'inigo_munoa',      name: 'Iñigo Muñoa',      dorsal: 15,   position: 'DEFENDER' },
  { id: 'aitor_salaberria', name: 'Aitor Salaberria', dorsal: 17,   position: 'FORWARD' },
  { id: 'patxi_elorza',     name: 'Patxi Elorza',     dorsal: null, position: 'MANAGER' }
]

var STARTING_XI = [
  'iker_mendiola', 'unai_larrea', 'mikel_otxoa', 'jon_zabala', 'benat_uriarte',
  'oier_agirre', 'ander_etxeberria', 'julen_arrieta', 'eneko_garmendia',
  'xabi_lasa', 'gorka_ibarra'
]
var USUAL_BENCH = ['asier_goitia', 'markel_bilbao', 'inigo_munoa', 'aitor_salaberria']

/*
 * A match declares what happened, not what each player ends up with: the per
 * player documents are derived from these events, so the numbers cannot drift
 * away from the score. `score: null` means it has not been played yet.
 */
var MATCHES = [
  {
    id: 'l01',
    competition: { type: 'LEAGUE', journey: 1 },
    date: '2026-08-16T10:00:00Z',
    local: APP_TEAM, visitor: 'lakua',
    score: { local: 3, visitor: 1 },
    events: {
      goals: { eneko_garmendia: 2, gorka_ibarra: 1 },
      assists: { xabi_lasa: 2, ander_etxeberria: 1 },
      yellowCards: { jon_zabala: 1 },
      saves: { iker_mendiola: 4 }
    }
  },
  {
    id: 'l02',
    competition: { type: 'LEAGUE', journey: 2 },
    date: '2026-08-23T12:00:00Z',
    local: 'zaramaga', visitor: APP_TEAM,
    score: { local: 0, visitor: 2 },
    events: {
      goals: { xabi_lasa: 1, aitor_salaberria: 1 },
      assists: { gorka_ibarra: 1 },
      penaltiesProvoked: { gorka_ibarra: 1 },
      saves: { iker_mendiola: 6 },
      bench: ['asier_goitia', 'markel_bilbao', 'aitor_salaberria']
    }
  },
  {
    id: 'l03',
    competition: { type: 'LEAGUE', journey: 3 },
    date: '2026-08-30T10:00:00Z',
    local: APP_TEAM, visitor: 'abetxuko',
    score: { local: 1, visitor: 1 },
    events: {
      goals: { ander_etxeberria: 1 },
      assists: { unai_larrea: 1 },
      yellowCards: { julen_arrieta: 1, oier_agirre: 1 },
      fails: { eneko_garmendia: 1 },
      saves: { iker_mendiola: 3 }
    }
  },
  {
    id: 'l04',
    competition: { type: 'LEAGUE', journey: 4 },
    date: '2026-09-06T12:00:00Z',
    local: 'judimendi', visitor: APP_TEAM,
    score: { local: 4, visitor: 2 },
    events: {
      goals: { eneko_garmendia: 1, markel_bilbao: 1 },
      assists: { ander_etxeberria: 1 },
      goalsProvoked: { mikel_otxoa: 1 },
      yellowCards: { benat_uriarte: 1, mikel_otxoa: 1 },
      redCards: { mikel_otxoa: 1 },
      saves: { iker_mendiola: 2 },
      fails: { jon_zabala: 2 }
    }
  },
  {
    id: 'l05',
    competition: { type: 'LEAGUE', journey: 5 },
    date: '2026-09-13T10:00:00Z',
    local: APP_TEAM, visitor: 'salburua',
    score: { local: 2, visitor: 0 },
    events: {
      goals: { gorka_ibarra: 2 },
      assists: { eneko_garmendia: 1, julen_arrieta: 1 },
      saves: { iker_mendiola: 5 }
    }
  },
  {
    id: 'cup01',
    competition: { type: 'CUP', name: 'Copa Gasteiz', round: 'Cuartos' },
    date: '2026-09-16T19:00:00Z',
    local: APP_TEAM, visitor: 'lakua',
    score: { local: 1, visitor: 0 },
    events: {
      // the cup gives minutes to the second goalkeeper: he keeps the clean sheet
      startersOverride: [
        'asier_goitia', 'unai_larrea', 'inigo_munoa', 'jon_zabala', 'benat_uriarte',
        'oier_agirre', 'ander_etxeberria', 'markel_bilbao', 'aitor_salaberria',
        'xabi_lasa', 'gorka_ibarra'
      ],
      bench: ['iker_mendiola', 'mikel_otxoa', 'eneko_garmendia'],
      goals: { aitor_salaberria: 1 },
      assists: { xabi_lasa: 1 },
      saves: { asier_goitia: 7 }
    }
  },
  {
    id: 'l06',
    competition: { type: 'LEAGUE', journey: 6 },
    date: '2026-09-27T10:00:00Z',
    local: APP_TEAM, visitor: 'zaramaga',
    score: null // not played yet: no score, no lineup
  }
]

var EMPTY_STATS = {
  assists: 0,
  cleanSheets: 0,
  fails: 0,
  goals: 0,
  goalsProvoked: 0,
  penaltiesProvoked: 0,
  redCards: 0,
  saves: 0,
  yellowCards: 0
}

var STAT_KEYS = Object.keys(EMPTY_STATS)

function playerById(id) {
  return PLAYERS.filter(function (player) { return player.id === id })[0]
}

function isHome(match) {
  return match.local === APP_TEAM
}

function goalsFor(match) {
  return isHome(match) ? match.score.local : match.score.visitor
}

function goalsAgainst(match) {
  return isHome(match) ? match.score.visitor : match.score.local
}

/*
 * Expands a match into one document per player involved. A player who took part
 * has a document with zeros; a player who did not is simply absent, which is what
 * tells "played and did nothing" apart from "was not called up".
 */
function lineupOf(match) {
  var events = match.events || {}
  var starters = events.startersOverride || STARTING_XI
  var bench = events.bench || USUAL_BENCH
  var manager = 'patxi_elorza'

  var docs = []

  starters.forEach(function (playerId, index) {
    docs.push(lineupDoc(match, playerId, 'STARTER', index + 1))
  })
  bench.forEach(function (playerId) {
    docs.push(lineupDoc(match, playerId, 'BENCH', null))
  })
  docs.push(lineupDoc(match, manager, 'MANAGER', null))

  // the goalkeeper who started keeps the clean sheet when nobody scored
  if (goalsAgainst(match) === 0) {
    var keeper = docs.filter(function (doc) {
      return doc.role === 'STARTER' && playerById(doc.playerId).position === 'GOALKEEPER'
    })[0]
    if (keeper) keeper.cleanSheets = 1
  }

  return docs
}

function lineupDoc(match, playerId, role, slot) {
  var doc = {
    playerId: playerId,
    matchId: matchDocId(match),
    seasonId: SEASON_ID,
    role: role,
    slot: slot
  }
  STAT_KEYS.forEach(function (key) {
    var scored = (match.events || {})[key] || {}
    doc[key] = scored[playerId] || 0
  })
  return doc
}

function matchDocId(match) {
  return SEASON_ID + '_' + match.id
}

/*
 * Every document the seed writes, as { path, data }. Timestamps are left to the
 * writer, which uses the server clock rather than this machine's.
 */
function buildDocuments() {
  var docs = []
  var seasonPath = 'seasons/' + SEASON_ID

  docs.push({ path: seasonPath, data: SEASON })

  PLAYERS.forEach(function (player) {
    docs.push({
      path: 'players/' + player.id,
      data: { name: player.name, faceImage: null, bodyImage: null }
    })
    docs.push({
      path: seasonPath + '/squad/' + player.id,
      data: { playerId: player.id, dorsal: player.dorsal, position: player.position }
    })
  })

  TEAMS.forEach(function (team) {
    docs.push({
      path: seasonPath + '/teams/' + team.id,
      data: { name: team.name, logo: team.logo }
    })
  })

  MATCHES.forEach(function (match) {
    var matchPath = seasonPath + '/matches/' + matchDocId(match)
    docs.push({
      path: matchPath,
      data: {
        competition: match.competition,
        date: new Date(match.date),
        localTeam: match.local,
        visitorTeam: match.visitor,
        score: match.score,
        deletedAt: null
      }
    })

    if (!match.score) return // nothing happened yet

    lineupOf(match).forEach(function (entry) {
      docs.push({ path: matchPath + '/lineup/' + entry.playerId, data: entry })
    })
  })

  return docs
}

/*
 * The invariants the data has to keep. They run before anything is written:
 * a seed that contradicts itself teaches the wrong thing.
 */
function verify() {
  var errors = []
  var squad = {}
  PLAYERS.forEach(function (player) { squad[player.id] = true })

  MATCHES.forEach(function (match) {
    var id = matchDocId(match)

    if (!match.score) {
      if (match.events) errors.push(id + ': not played, but it carries events')
      return
    }

    var lineup = lineupOf(match)
    var seen = {}

    lineup.forEach(function (entry) {
      if (!squad[entry.playerId]) errors.push(id + ': ' + entry.playerId + ' is not in the squad')
      if (seen[entry.playerId]) errors.push(id + ': ' + entry.playerId + ' appears twice')
      seen[entry.playerId] = true
    })

    var scored = lineup.reduce(function (total, entry) { return total + entry.goals }, 0)
    if (scored !== goalsFor(match)) {
      errors.push(id + ': players scored ' + scored + ' but the score says ' + goalsFor(match))
    }

    var assists = lineup.reduce(function (total, entry) { return total + entry.assists }, 0)
    if (assists > scored) {
      errors.push(id + ': ' + assists + ' assists for ' + scored + ' goals')
    }

    Object.keys(match.events || {}).forEach(function (key) {
      if (key === 'bench' || key === 'startersOverride') return
      if (STAT_KEYS.indexOf(key) === -1) errors.push(id + ': unknown event "' + key + '"')
      Object.keys(match.events[key]).forEach(function (playerId) {
        if (!seen[playerId]) errors.push(id + ': ' + playerId + ' has ' + key + ' but did not play')
      })
    })
  })

  return errors
}

if (typeof module !== 'undefined') {
  module.exports = {
    SEASON_ID: SEASON_ID,
    APP_TEAM: APP_TEAM,
    MATCHES: MATCHES,
    buildDocuments: buildDocuments,
    verify: verify
  }
}
