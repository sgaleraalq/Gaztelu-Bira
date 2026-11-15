# Notificaciones de partido nuevo

Cuando se inserta un partido, a todo el que tenga sesión iniciada le llega un push:

> **Gaztelu Bira 2 - 1 Ecovaldi**
> Jornada 8 · Ya puedes ver el partido en la app

Una idea lo gobierna entero:

> **Ninguna app envía notificaciones.** Las apps escriben el partido, como siempre, y
> una Cloud Function que vigila Firestore se entera y manda el push.

Por eso esto no toca ninguna pantalla de inserción: da igual que el partido lo escriba
Android, iOS o un script, y da igual cómo esté hecha la pantalla. Mientras el documento
aparezca en `seasons/{season}/matches/{match}`, el push sale.

## Las tres piezas

```
   escribe el partido                    publica en el topic "matches"
App ───────────────► Firestore ───► Cloud Function ───► FCM ───► Android / iOS
                                    client/functions
```

| pieza | dónde | qué sabe |
|---|---|---|
| Quien envía | `client/functions` | Firestore y FCM. Nada de las apps. |
| Android | `android/core/notifications` | FCM. No depende de ningún otro módulo. |
| iOS | `ios/GazteluBira/Notifications` | APNs y FCM. No depende de nada del resto. |

Ninguna de las tres conoce la sesión ni la navegación. Lo único que comparten es el
contrato de abajo.

## El contrato

| | valor |
|---|---|
| topic | `matches` |
| canal de Android | `matches` |
| `data.type` | `match_inserted` |
| `data.seasonId` | `2026_2027` |
| `data.matchId` | `2026_2027_l08` |

Está escrito en tres sitios, y un cambio hay que hacerlo en los tres:
`match-notification.js`, `MatchNotificationContract.kt` y `MatchNotificationPayload.swift`.

El texto viaja en el bloque `notification` del mensaje, así que lo compone la función
y las apps no tienen que saber redactarlo: con la app cerrada lo pinta el sistema.

## Cuándo sale un push

La primera vez que un partido tiene resultado:

- se inserta ya jugado, que es lo que hace la pantalla de inserción, o
- estaba en el calendario con `score: null` y ahora recibe su marcador.

Y cuándo **no**:

- el partido solo está programado (`score: null`);
- se corrige un partido que ya tenía resultado;
- se borra, o llega con `deletedAt`;
- **el partido tiene más de 14 días** (`MAX_AGE_DAYS`). Esto es lo que evita que
  `npm run upload` o restaurar una copia mande un push por cada partido de la
  temporada. La contrapartida: un partido que se sube con tres semanas de retraso no
  avisa a nadie.

Las reglas están en `shouldNotify`, y cada una tiene su test.

## Desplegarlo

Las Cloud Functions necesitan el **plan Blaze** (de pago por uso) en el proyecto de
Firebase. Con el volumen de este equipo — un push a la semana — el coste es cero, pero
hay que tener una tarjeta puesta.

```bash
npm install -g firebase-tools
firebase login

cd client/functions
npm install
npm test                  # las reglas, sin tocar Firebase

cd ..
firebase deploy --only functions --project gaztelu-bira
```

La región está en `REGION`, en `index.js` (`europe-west1`). Solo decide en qué máquina
corre la función; lo ideal es la más cercana a la base de datos.

Para ver qué ha hecho:

```bash
firebase functions:log --only notifyMatchInserted --project gaztelu-bira
```

Cada escritura deja una línea: `Push sent` con el título, o `No push for this write`.

## Android

Ya está conectado en esta rama. Son cuatro puntos de contacto con el resto:

- `settings.gradle.kts` incluye `:core:notifications`;
- `libs.versions.toml` gana `firebase-messaging` y `androidx-activity`;
- `:gaztelu_bira` depende del módulo;
- `GazteluBiraActivity` llama a `sessionNotifications.follow(this)`.

`SessionNotifications`, en el módulo de la app, es el único sitio donde los pushes se
cruzan con el resto: al iniciar sesión pide el permiso (Android 13+) y se suscribe, y
al cerrarla se da de baja. Quitar las notificaciones es borrar esa llamada.

No hace falta nada en la consola de Firebase: FCM funciona con el
`google-services.json` que ya hay.

## iOS

La carpeta `ios/GazteluBira/Notifications` compila sola, tenga el proyecto Firebase o
no (`#if canImport(FirebaseMessaging)`), pero no hace nada hasta que se conecta. En
`main` iOS todavía no tiene Firebase, así que estos pasos son para cuando esta rama se
junte con la de iOS:

1. **Enlazar FirebaseMessaging.** Target `GazteluBira` → General → Frameworks,
   Libraries, and Embedded Content → `+` → `FirebaseMessaging` (del paquete
   `firebase-ios-sdk` que ya está).
2. **Capability.** Signing & Capabilities → `+ Capability` → Push Notifications.
3. **Clave de APNs.** En developer.apple.com → Keys, crea una clave con *Apple Push
   Notifications service* y súbela en la consola de Firebase → Configuración del
   proyecto → Cloud Messaging → app de iOS. Sin ella FCM no puede entregar nada a iOS.
   Hace falta la cuenta de desarrollador de pago.
4. **El delegate**, en `GazteluBiraApp`:

   ```swift
   @UIApplicationDelegateAdaptor(NotificationsAppDelegate.self) private var notifications
   ```

5. **Seguir la sesión**, igual que `SessionNotifications` en Android. En el
   `WindowGroup`, sobre `MainScreen`:

   ```swift
   .task {
       var signedIn = false
       for await session in container.sessionManager.sessionStream() {
           let isSignedIn = session != nil
           guard isSignedIn != signedIn else { continue }
           signedIn = isSignedIn

           if isSignedIn {
               await MatchNotifications.shared.subscribe()
           } else {
               await MatchNotifications.shared.unsubscribe()
           }
       }
   }
   ```

   La sesión empieza vacía en cada arranque hasta que se carga el perfil; como
   `signedIn` empieza en `false`, ese primer vacío no cuenta como cerrar sesión.

Los pushes no llegan al simulador con una clave de APNs: hay que probar en un
dispositivo.

## Lo que no hace todavía

- **Abrir el partido al tocar la notificación.** Hoy abre la app, sin más. El dato
  está servido para quien quiera navegar — `MatchNotificationPayload.fromIntent(intent)`
  en Android, `MatchNotifications.shared.openedMatch` en iOS —, pero no es solo
  navegar: el partido nuevo todavía no está en la caché local cuando se toca, así que
  antes hay que sincronizarlo.
- **Refrescar con la app abierta.** Si el push llega con la app en primer plano, el
  partido no aparece hasta el siguiente arranque, que es cuando se sincroniza.
- **Elegir qué avisos recibir.** Hay un solo topic. Un ajuste de "silenciar" sería
  llamar a `unsubscribe()`.
