# La base de datos

Firestore, proyecto **`gaztelu-bira`**. Una idea la gobierna entera:

> Se guarda **lo que pasó**. Todo lo que se pueda calcular a partir de eso —
> clasificación, totales, rachas, porcentajes — no se guarda: se deriva al leer.

De ahí salen las tres decisiones que explican el resto: los partidos guardan
marcador y no resultado, los jugadores guardan estadísticas por partido y no
acumulados, y la tabla de la liga no existe como documento.

## El árbol

```
seasons/{seasonId}                      la temporada
  teams/{teamId}                          equipos de esa liga
  squad/{playerId}                        ficha del jugador ESA temporada
  matches/{matchId}                       partidos
    lineup/{playerId}                       quién jugó y qué hizo

players/{playerId}                      la persona, fuera de temporada
users/{uid}                             cuentas de la app
```

Lo que cuelga de `seasons/` muere con la temporada; lo que está fuera, no. Por eso
el jugador está partido en dos: `players/{id}` es quien es (nombre, fotos) y
`squad/{id}` es lo que fue ese año (dorsal, posición). Cuando empiece la temporada
siguiente, Iker sigue siendo el mismo Iker aunque cambie de dorsal.

---

## Temporada — `seasons/{seasonId}`

| campo | tipo | qué es |
|---|---|---|
| `name` | String | "Temporada 2026-2027" |
| `startsAt` / `endsAt` | Timestamp | rango de la temporada |
| `isCurrent` | Boolean | la que la app abre por defecto |
| `updatedAt` | Timestamp | lo pone el servidor |

**Id**: `2026_2027`. Lo eliges tú, es legible y ordena bien.

**Cómo se crea**: a mano, o con el seed. Es un documento al año.

`isCurrent` es lo que sustituye al `"2024_2025"` que antes estaba escrito en el
código: la app pregunta cuál es la temporada actual en vez de saberlo.

## Equipos — `seasons/{seasonId}/teams/{teamId}`

| campo | tipo |
|---|---|
| `name` | String |
| `logo` | String? (URL de Storage) |
| `updatedAt` | Timestamp |

**Id**: el nombre en minúsculas sin espacios (`lakua`, `gaztelu_bira`).

**Cómo se crean**: una vez por temporada, al conocer los rivales de la liga. Van
dentro de la temporada a propósito: los rivales cambian cada año, y el mismo club
puede estar en otra categoría.

## Jugadores — `players/{playerId}`

| campo | tipo | qué es |
|---|---|---|
| `name` | String | nombre completo |
| `nickname` | String? | cómo le llama el equipo; es lo que pinta la UI cuando existe |
| `faceImage` | String? | URL de Storage |
| `bodyImage` | String? | |
| `updatedAt` | Timestamp | |

**Id**: `pedro_garces`, estable para siempre.

El `nickname` está aquí y no en `squad/` a propósito: el apodo es de la persona y no
cambia de temporada, igual que el nombre. Lo que cambia cada año es el dorsal.

**Cómo se crean**: una vez, cuando la persona entra en el club. No se duplica cada
temporada.

## Plantilla — `seasons/{seasonId}/squad/{playerId}`

| campo | tipo |
|---|---|
| `playerId` | String (el mismo que el id del documento) |
| `dorsal` | Int? (el entrenador no tiene) |
| `position` | String: `GOALKEEPER`, `DEFENDER`, `MIDFIELDER`, `FORWARD`, `MANAGER` |
| `updatedAt` | Timestamp |

**Id**: el mismo `playerId`, para que la unión sea directa.

**Cómo se crea**: al empezar la temporada, uno por cada persona de la plantilla.
Si alguien se va en enero, se borra su ficha de la temporada — pero su `players/`
y todo lo que jugó siguen intactos.

## Partidos — `seasons/{seasonId}/matches/{matchId}`

| campo | tipo | qué es |
|---|---|---|
| `competition` | Map | `{ type: "LEAGUE", journey: 5 }` o `{ type: "CUP", name: "Copa Gasteiz", round: "Cuartos" }` |
| `date` | Timestamp | hora de inicio |
| `localTeam` / `visitorTeam` | String | ids de `teams/` |
| `score` | Map? | `{ local: 2, visitor: 0 }`, o **`null` si no se ha jugado** |
| `deletedAt` | Timestamp? | marca de borrado |
| `updatedAt` | Timestamp | |

**Id**: determinista — `2026_2027_l05` para la jornada 5 de liga, `2026_2027_cup01`
para copa. Esto importa: si dos personas suben la misma jornada desde dos móviles,
escriben **el mismo documento** en vez de crear dos jornadas 5.

**Cómo se crea**: al terminar el partido, desde la pantalla de inserción, en el
mismo batch que su alineación. También puede crearse antes con `score: null` para
tener el calendario.

Dos detalles que vienen de haberse quemado antes:

- **`score: null` no es 0-0.** Un partido programado y un empate a cero son cosas
  distintas, y la clasificación solo cuenta los que tienen marcador.
- **La jornada es un número dentro de `competition`**, no el texto "Jornada 5", y
  solo existe en liga. La copa guarda nombre y ronda, que es lo que sí tiene.

## Alineación — `seasons/{seasonId}/matches/{matchId}/lineup/{playerId}`

Un documento por jugador **que estuvo en la convocatoria**.

`cleanSheets` es el único contador con decimales, porque se reparte por medias
partes: quien solo jugó la segunda y no encajó se lleva 0.5. Firestore guardará
un `1` como entero y un `0.5` como decimal —no hay forma de forzar el tipo desde
el cliente—, así que el campo se lee siempre como `Double`, nunca como `Int`.

| campo | tipo | qué es |
|---|---|---|
| `playerId`, `matchId`, `seasonId` | String | repetidos a propósito, ver abajo |
| `role` | String | `STARTER`, `BENCH`, `MANAGER` |
| `slot` | Int? | puesto en la alineación, solo titulares |
| `goals`, `assists`, `goalsProvoked`, `penaltiesProvoked`, `saves`, `fails`, `yellowCards`, `redCards` | Int | lo que hizo ese día |
| `cleanSheets` | Double | **medias partes**: 0.5 por cada una sin encajar, 1 el partido entero |
| `updatedAt` | Timestamp | |

**Id**: el `playerId`. Así un jugador no puede aparecer dos veces en el mismo
partido, lo impide la propia base de datos.

Los tres ids repetidos son la razón de que las consultas sean baratas: con ellos,
una sola consulta de *collection group* responde tanto "todas las estadísticas de
la temporada" como "la ficha de este jugador", sin recorrer partido por partido.

---

## ¿Y si un jugador no juega?

**Exacto: si no fue convocado, no se guarda nada suyo en ese partido.** No hay
documento. Y eso es información, no un hueco:

| situación | qué hay en `lineup/` |
|---|---|
| No convocado | **nada**, el documento no existe |
| Convocado al banquillo, no sale | documento con `role: BENCH` y las estadísticas a cero |
| Titular sin participar en ninguna jugada | documento con `role: STARTER` y ceros |
| Entrenador | documento con `role: MANAGER` y ceros |

La diferencia clave: **la ausencia de documento significa "no estuvo"; un documento
con ceros significa "estuvo y no hizo nada"**. Son dos hechos distintos y la base
de datos los distingue.

Es justo lo contrario del esquema anterior, donde `createPlayerStats` recorría
*toda* la plantilla y escribía una fila por cada jugador en cada partido. Con 25
jugadores y 30 jornadas eran 750 documentos, la mayoría todo ceros, y no había
forma de saber quién había ido convocado.

Una limitación que conviene conocer: `role: BENCH` dice que estuvo convocado, **no
si llegó a saltar al campo**. Hoy el dominio (`PlayerRole.countsAsPlayed`) cuenta
como partido jugado a titulares y suplentes. Si algún día quieres separar "fue
convocado" de "jugó minutos", el sitio es un campo más en este documento
(`minutesPlayed` o `cameOn`), no un cambio de estructura.

---

## Reglas que valen para todo

**`updatedAt` lo pone el servidor.** Nunca el móvil: en Kotlin es
`@ServerTimestamp` sobre un campo nulo, y en los scripts `FieldValue.serverTimestamp()`.
Si lo pusiera el dispositivo, un reloj atrasado publicaría cambios que nadie más
llega a descargarse.

**Nada se borra a lo bruto.** Se marca con `deletedAt` y el cliente lo elimina de
su copia local. Así un error de red no se puede confundir con "esto ya no existe".

**La sincronización es por diferencia.** El cliente guarda cuándo sincronizó por
última vez y pide solo lo posterior:

```js
db.collection('seasons/2026_2027/matches').where('updatedAt', '>', ultimaVez)
```

No se descarga la temporada entera cada vez.

## Consultas típicas

```js
// la temporada en curso
db.collection('seasons').where('isCurrent', '==', true).limit(1)

// solo liga, en orden de jornada
db.collection('seasons/2026_2027/matches').where('competition.type', '==', 'LEAGUE').orderBy('date')

// todo lo que ha hecho un jugador esta temporada, en una consulta
db.collectionGroup('lineup').where('seasonId', '==', '2026_2027').where('playerId', '==', 'gorka_ibarra')

// todas las estadísticas de la temporada que han cambiado desde X
db.collectionGroup('lineup').where('seasonId', '==', '2026_2027').where('updatedAt', '>', ultimaVez)
```

Las de `collectionGroup` y la de liga ordenada necesitan índice compuesto: están
declarados en [firestore.indexes.json](firestore.indexes.json).

## Correspondencia con el dominio

| Firestore | Kotlin (`core.domain.migration.model`) |
|---|---|
| `seasons/{s}` | `season.Season` |
| `seasons/{s}/teams/{t}` | `season.team.Team` |
| `seasons/{s}/squad/{p}` | `season.squad.SeasonPlayer` |
| `seasons/{s}/matches/{m}` | `season.match.Match` + `Competition` + `Score` |
| `…/matches/{m}/lineup/{p}` | `season.match.lineup.MatchPlayer` + `PlayerRole` |
| `players/{p}` | `player.Player` |

El id del documento **no** es un campo del modelo de red: lo pasa el mapper
(`asModel(id: PlayerId)`), porque quien lee la ruta ya lo conoce.

## Seguridad e índices

[firestore.rules](firestore.rules) — lee cualquiera identificado, escribe solo
quien tenga el *custom claim* `admin` en su token. Los scripts van por el Admin
SDK, que se salta las reglas, así que sembrar no depende de tener permisos.

```bash
firebase deploy --only firestore:rules,firestore:indexes
```

## Entornos

`gaztelu-bira` es el proyecto de pruebas, y la app de debug
(`com.sgale.gaztelubira.debug`) apunta ahí con su propio `google-services.json` en
`android/gaztelu_bira/src/debug/`. Los datos reales viven en otro proyecto, así que
no hay forma de escribir en producción desde una build de desarrollo.

Para llenar este proyecto desde cero: [firestore-seed/](firestore-seed/README.md).
