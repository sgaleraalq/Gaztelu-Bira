# Temporada de ejemplo para Firestore

Crea una temporada completa con la estructura `seasons/{season}` para poder trastear con
consultas reales antes de migrar nada. **No toca la colección `2024_2025` que usa la app hoy**:
escribe en `seasons/`, así que las dos pueden convivir.

## Qué crea

```
seasons/2026_2027                                { name, startsAt, endsAt, isCurrent, updatedAt }
  teams/{teamId}              6 equipos          { name, logo, updatedAt }
  squad/{playerId}           16 fichas           { playerId, dorsal, position, updatedAt }
  matches/{matchId}           7 partidos         { competition, date, localTeam, visitorTeam,
                                                   score|null, deletedAt, updatedAt }
    lineup/{playerId}        ~15 por partido     { playerId, matchId, seasonId, role, slot,
                                                   goals, assists, saves, …, updatedAt }
players/{playerId}           16 personas         { name, faceImage, bodyImage, updatedAt }
```

140 documentos en total.

## Cómo ejecutarlo

Necesitas Node 18+. Sin instalar nada de Firebase puedes ver lo que escribiría:

```bash
npm install
npm run dry-run
```

Contra el emulador (lo recomendado para familiarizarse — datos de usar y tirar, y la consola
en http://127.0.0.1:4000):

```bash
firebase emulators:start --only firestore
npm run seed          # usa 127.0.0.1:8080 salvo que pongas FIRESTORE_EMULATOR_HOST
npm run reseed        # borra la temporada y la vuelve a escribir
```

Contra un proyecto real hay que pedirlo en voz alta, y aun así con credenciales:

```bash
export GOOGLE_APPLICATION_CREDENTIALS=/ruta/service-account.json
node seed.js --production --project gbmultiplatform --yes
```

Sin `--yes` se niega. Sin `--production` va siempre al emulador.

## Qué está montado a propósito

El dataset no es relleno: cada cosa está para que veas cómo se comporta una decisión del esquema.

- **`2026_2027_l06` no se ha jugado**: `score: null` y **sin subcolección `lineup`**. Es lo que
  distingue un partido pendiente de un 0-0, que hoy no se puede distinguir.
- **`cup01` es de copa** (`competition.type == 'CUP'`), con nombre y ronda en vez de jornada.
  Sirve para comprobar que la clasificación lo excluye.
- **La jornada es un número** dentro de `competition`, no el texto "Jornada 6".
- **Los ids son deterministas** (`2026_2027_l01`): dos personas insertando la misma jornada
  escriben el mismo documento en vez de crear dos.
- **`lineup` solo tiene a los implicados.** Quien no fue convocado no aparece — eso distingue
  "jugó y no hizo nada" de "no estuvo".
- **En la copa juega el segundo portero** (Asier Goitia) y se lleva la portería a cero, para que
  se vea que la estadística es del partido, no del titular habitual.
- **Mikel Otxoa ve roja en `l04`** y ahí también hay un gol en propia (`goalsProvoked`).
- **`deletedAt`** viaja en todos los partidos: el borrado se marca, no se deduce de una ausencia.

`data.js` comprueba estas invariantes **antes** de escribir, y aborta si alguna falla:
los goles de los jugadores cuadran con el marcador, nadie aparece dos veces en un partido,
nadie tiene estadísticas sin haber jugado y un partido sin jugar no trae eventos.

## Consultas para trastear

Sincronización por delta — lo que sustituye al `syncItems` actual:

```js
db.collection('seasons/2026_2027/matches').where('updatedAt', '>', ultimaVez)
```

Ficha de un jugador en toda la temporada, en **una** consulta:

```js
db.collectionGroup('lineup')
  .where('seasonId', '==', '2026_2027')
  .where('playerId', '==', 'gorka_ibarra')
```

Solo liga, para la clasificación:

```js
db.collection('seasons/2026_2027/matches').where('competition.type', '==', 'LEAGUE')
```

Las dos últimas piden un índice compuesto; Firestore te da el enlace para crearlo en el mensaje
de error la primera vez (en el emulador no hace falta).

## Copia de seguridad de lo que ya tienes

`dump.js` se lleva a un JSON lo que haya en Firestore, subcolecciones incluidas, y `restore.js`
lo vuelve a escribir. Leer no hace daño, así que el dump no pide permiso; restaurar sobre datos
reales sí.

```bash
export GOOGLE_APPLICATION_CREDENTIALS=/ruta/service-account.json

node dump.js --project gbmultiplatform 2024_2025 users   # solo esas colecciones
node dump.js --project gbmultiplatform                   # todas las raíces
node dump.js --emulator                                  # lo que tengas en el emulador
```

Sale un `backup-AAAA-MM-DD.json` con una entrada por documento, la ruta como clave:

```json
"2024_2025/information/matches/m1": {
  "matchName": "Jornada 1",
  "date": { "__firestore": "timestamp", "seconds": 1758326400, "nanoseconds": 0 }
}
```

Los tipos que JSON no sabe guardar — `Timestamp`, `GeoPoint`, referencias, bytes — van
etiquetados con `__firestore` para que la vuelta sea exacta y no se conviertan en números o
cadenas por el camino. Los documentos vacíos también se guardan: Firestore los usa como percha
de sus subcolecciones, y saltárselos dejaría huérfano lo que cuelga debajo (tu `2024_2025/stats`
es justo uno de esos).

Para volver:

```bash
node restore.js --in backup-2026-09-20.json                        # al emulador
node restore.js --in backup.json --prefix 2024_2025/information    # solo una rama
node restore.js --in backup.json --production --project X --yes    # sobre el proyecto real
```

Sin `--production` va siempre al emulador, y con `--production` pero sin `--yes` se niega.
**El uso interesante es el del medio**: te bajas producción y la restauras en el emulador, y a
partir de ahí puedes romper lo que quieras con datos de verdad.

Dos avisos: `restore.js` escribe encima de lo que haya en esas rutas pero **no borra** lo que
sobre, así que no es una vuelta atrás exacta si entre medias se creó algo nuevo. Y esto es una
copia hecha por un cliente, no la exportación gestionada de Google — si algún día el volumen
crece, lo que toca es `gcloud firestore export gs://tu-bucket`, que va por servidor y es
consistente, pero pide plan Blaze y un bucket.

## Lo que no crea

`users/{uid}`, porque el uid lo pone Firebase Auth: sembrar uno inventado no te enseña nada y
encima estorba cuando entres de verdad. El rol de admin va como *custom claim* del token,
no como documento.
