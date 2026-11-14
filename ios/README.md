# Gaztelu Bira — iOS

La app de iOS es el espejo de `android/`: las pantallas se dibujan con el Compose
compartido de `multiplatform/ui`, y todo lo que hay por debajo —dominio, caché local,
Firebase, navegación y view models— está escrito en Swift con la misma forma que los
módulos `:core:*` de Android.

| Android | iOS |
|---|---|
| `:core:common` | `GazteluBira/Core/Common` |
| `:core:domain` | `GazteluBira/Core/Domain` |
| `:core:database` (Room) | `GazteluBira/Core/Database` (SQLite del sistema, mismas tablas) |
| `:core:network` (Firebase) | `GazteluBira/Core/Network` |
| `:core:navigation` (Navigation 3) | `GazteluBira/Core/Navigation` (`NavigationStack`) |
| `:core:screens` (pantallas + view models) | `GazteluBira/Core/Screens` |
| `:gaztelu_bira` (Hilt, `FirstTime`) | `GazteluBira/App` (`AppContainer`, `FirstTime`) |

## Cómo se une Swift con Compose

Un `@Composable` no puede cruzar a Swift, así que `multiplatform/ui/src/iosMain/.../ui/ios`
expone cada pantalla como un `UIViewController`:

- `IosState<T>` lleva el estado: el view model de Swift escribe en `value` y Compose se
  recompone.
- Las acciones llegan como lambdas sueltas, y el `XActions` se monta en Kotlin.
- `IosRoute` es el `UiDestination` aplanado; `IosSealedStates.kt` e `IosUiStates.kt`
  dan a Swift los estados iniciales y los miembros de las jerarquías selladas.

## Antes de compilar

1. **`GoogleService-Info.plist`**. Descárgalo de la consola de Firebase (app de iOS con
   bundle id `com.sgale.gaztelubira`) y déjalo en `ios/GazteluBira/`. No se sube al
   repositorio, igual que el `google-services.json` de Android. Sin él la app se cierra
   al arrancar.
2. **Google Sign-In**. En el target `GazteluBira` → Build Settings, pon en
   `GOOGLE_REVERSED_CLIENT_ID` el valor de `REVERSED_CLIENT_ID` de ese plist. Sin él el
   botón de Google no hace nada (queda registrado en el log).
3. Abre `GazteluBira.xcodeproj`. Xcode resuelve solo los paquetes (`firebase-ios-sdk` y
   `GoogleSignIn-iOS`) y la fase *Compile Kotlin Framework* construye `GazteluBiraUI`.

## Lo que falta respecto a Android

- **Insertar partido**: en Android esa pantalla sigue en Compose solo-Android
  (`core/screens/insert/match`). Hasta que exista un `InsertMatchView` compartido, iOS
  muestra un aviso de "no disponible".
- **Revisar foto / cámara**: tampoco están conectadas en Android.
- **Actualizar la app**: `openStoreListing()` necesita el id de App Store (`Utils.swift`).
- **Crashlytics**: el SDK está enlazado, pero falta la fase de subida de dSYM para ver
  los cierres simbolizados.

## Diferencias que son de la plataforma

- **Atrás**: iOS no tiene botón. Deslizar desde el borde izquierdo vuelve atrás, y en
  la home hace lo que el botón atrás de Android: preguntar si cerrar sesión.
- **Toasts**: iOS no tiene; `ToastCenter` dibuja uno propio.
- **Galería**: se usa el selector de fotos del sistema, que no pide permisos.
