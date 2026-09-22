/*
 * Sample season for the Firestore layout: seasons/{season}/{teams,squad,matches/{match}/lineup}.
 *
 * Everything here is pure data and pure functions, so it can be inspected and
 * checked without touching Firebase at all. `seed.js` is what writes it.
 */

var SEASON_ID = '2026_2027'
var APP_TEAM = 'gaztelu_bira'

var SEASON = {
  name: '26/27',
  startsAt: new Date('2026-08-01T00:00:00Z'),
  endsAt: new Date('2026-06-30T00:00:00Z'),
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
  { id: 'pedro_garces',          name: 'Pedro Garces',             nickname: 'Pedro',       dorsal: 1,    position: 'GOALKEEPER' },
  { id: 'jon_ortiz_de_urbina',   name: 'Jon Ortiz de Urbina',      nickname: 'Joni',        dorsal: 2,    position: 'UNDEFINED' },
  { id: 'asier_huarte',          name: 'Asier Huarte',             nickname: 'Asiertxi',    dorsal: 3,    position: 'UNDEFINED' },
  { id: 'carlos_cuesta',         name: 'Carlos Cuesta',            nickname: 'Carlos',      dorsal: 4,    position: 'UNDEFINED' },
  { id: 'emilio_galera',         name: 'Emilio Galera',            nickname: 'Emilio',      dorsal: 5,    position: 'UNDEFINED' },
  { id: 'adrian_perujo',         name: 'Adrián Perujo',            nickname: 'Perujo',      dorsal: 6,    position: 'UNDEFINED' },
  { id: 'fernando_ayala',        name: 'Fernando Javier Ayala',    nickname: 'Nando',       dorsal: 7,    position: 'UNDEFINED' },
  { id: 'julen_galera',          name: 'Julen Galera',             nickname: 'Julen',       dorsal: 8,    position: 'UNDEFINED' },
  { id: 'diego_morales',         name: 'Diego Morales',            nickname: 'Diego',       dorsal: 9,    position: 'UNDEFINED' },
  { id: 'mikel_agustino',        name: 'Mikel Agustino',           nickname: 'Mikel',       dorsal: 10,   position: 'UNDEFINED' },
  { id: 'gorka_arizpeleta',      name: 'Gorka Arizpeleta',         nickname: 'Gorka',       dorsal: 11,   position: 'UNDEFINED' },
  { id: 'dame_gueye',            name: 'Dame Gueye',               nickname: 'Dame',        dorsal: 12,   position: 'UNDEFINED' },
  { id: 'guille',                name: 'Guille',                   nickname: 'Guille',      dorsal: 13,   position: 'GOALKEEPER' },
  { id: 'aratz',                 name: 'Aratz',                    nickname: 'Aratz',       dorsal: 14,   position: 'UNDEFINED' },
  { id: 'sergio_galera',         name: 'Sergio Galera',            nickname: 'Haaland',     dorsal: 15,   position: 'UNDEFINED' },
  { id: 'dani_galera',           name: 'Dani Galera',              nickname: 'Dani',        dorsal: 16,   position: 'UNDEFINED' },
  { id: 'pablo_arrondo',         name: 'Pablo Arrondo',            nickname: 'Garru',       dorsal: 17,   position: 'UNDEFINED' },
  { id: 'xabi_galera',           name: 'Xabi Galera',              nickname: 'Xabi',        dorsal: 18,   position: 'UNDEFINED' },
  { id: 'david_sadaba',          name: 'David Sádaba',             nickname: 'Sádaba',      dorsal: 19,   position: 'UNDEFINED' },
  { id: 'alvaro_galera',         name: 'Alvaro Galera',            nickname: 'Alvaro',      dorsal: 20,   position: 'UNDEFINED' },
  { id: 'iker_vergara',          name: 'Iker Vergara',             nickname: 'Iker.',       dorsal: 21,   position: 'UNDEFINED' },
  { id: 'jon_huarte',            name: 'Jon Huarte',               nickname: 'Jon',         dorsal: 22,   position: 'UNDEFINED' },
  { id: 'adrian_orta',           name: 'Adrián Orta',              nickname: 'Adrián',      dorsal: 23,   position: 'UNDEFINED' },

  // sin dorsal
  { id: 'juan_carlos_huarte',    name: 'Juan Carlos Huarte',       nickname: 'Juan Carlos', dorsal: null, position: 'MANAGER' },
  { id: 'jose_angel_galera',     name: 'José Ángel Galera Balboa', nickname: 'José Ángel',  dorsal: null, position: 'UNDEFINED' },
  { id: 'joseba_ortiz_de_urbina', name: 'Joseba Ortiz de Urbina',  nickname: 'Joseba',      dorsal: null, position: 'UNDEFINED' },
  { id: 'juan_jose_medina',      name: 'Juan José Medina',         nickname: 'Juan José',   dorsal: null, position: 'UNDEFINED' },
  { id: 'unai_mugica',           name: 'Unai Múgica',              nickname: 'Unai',        dorsal: null, position: 'UNDEFINED' },
  { id: 'alexis_yanac',          name: 'Alexis Antonio Yañac',     nickname: 'Alexis',      dorsal: null, position: 'UNDEFINED' },
  { id: 'julen_maeztu',          name: 'Julen Maeztu',             nickname: 'Maeztu',      dorsal: null, position: 'UNDEFINED' },
  { id: 'xabier_lasaga',         name: 'Xabier Lasaga',            nickname: 'Lasaga',      dorsal: null, position: 'UNDEFINED' }
]

var STARTING_XI = [
  'pedro_garces', 'jon_ortiz_de_urbina', 'asier_huarte', 'carlos_cuesta', 'emilio_galera',
  'adrian_perujo', 'fernando_ayala', 'julen_galera', 'diego_morales',
  'mikel_agustino', 'gorka_arizpeleta'
]
var USUAL_BENCH = ['guille', 'aratz', 'sergio_galera', 'pablo_arrondo']

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
      goals: { diego_morales: 2, gorka_arizpeleta: 1 },
      assists: { mikel_agustino: 2, fernando_ayala: 1 },
      yellowCards: { carlos_cuesta: 1 },
      saves: { pedro_garces: 4 }
    }
  },
  {
    id: 'l02',
    competition: { type: 'LEAGUE', journey: 2 },
    date: '2026-08-23T12:00:00Z',
    local: 'zaramaga', visitor: APP_TEAM,
    score: { local: 0, visitor: 2 },
    events: {
      goals: { mikel_agustino: 1, pablo_arrondo: 1 },
      assists: { gorka_arizpeleta: 1 },
      penaltiesProvoked: { gorka_arizpeleta: 1 },
      saves: { pedro_garces: 6 },
      bench: ['guille', 'aratz', 'pablo_arrondo']
    }
  },
  {
    id: 'l03',
    competition: { type: 'LEAGUE', journey: 3 },
    date: '2026-08-30T10:00:00Z',
    local: APP_TEAM, visitor: 'abetxuko',
    score: { local: 1, visitor: 1 },
    events: {
      goals: { fernando_ayala: 1 },
      assists: { jon_ortiz_de_urbina: 1 },
      yellowCards: { julen_galera: 1, adrian_perujo: 1 },
      fails: { diego_morales: 1 },
      saves: { pedro_garces: 3 }
    }
  },
  {
    id: 'l04',
    competition: { type: 'LEAGUE', journey: 4 },
    date: '2026-09-06T12:00:00Z',
    local: 'judimendi', visitor: APP_TEAM,
    score: { local: 4, visitor: 2 },
    events: {
      goals: { diego_morales: 1, aratz: 1 },
      assists: { fernando_ayala: 1 },
      goalsProvoked: { asier_huarte: 1 },
      yellowCards: { emilio_galera: 1, asier_huarte: 1 },
      redCards: { asier_huarte: 1 },
      saves: { pedro_garces: 2 },
      fails: { carlos_cuesta: 2 }
    }
  },
  {
    id: 'l05',
    competition: { type: 'LEAGUE', journey: 5 },
    date: '2026-09-13T10:00:00Z',
    local: APP_TEAM, visitor: 'salburua',
    score: { local: 2, visitor: 0 },
    events: {
      goals: { gorka_arizpeleta: 2 },
      assists: { diego_morales: 1, julen_galera: 1 },
      saves: { pedro_garces: 5 }
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
        'guille', 'jon_ortiz_de_urbina', 'sergio_galera', 'carlos_cuesta', 'emilio_galera',
        'adrian_perujo', 'fernando_ayala', 'aratz', 'pablo_arrondo',
        'mikel_agustino', 'gorka_arizpeleta'
      ],
      bench: ['pedro_garces', 'asier_huarte', 'diego_morales'],
      goals: { pablo_arrondo: 1 },
      assists: { mikel_agustino: 1 },
      saves: { guille: 7 }
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
  var manager = 'juan_carlos_huarte'

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
      data: {
        name: player.name,
        nickname: player.nickname,
        faceImage: null,
        bodyImage: null
      }
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
