/*
 * Designed and developed by 2026 sgaleraalq (Sergio Galera)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import Foundation
import FirebaseFirestore
import FirebaseRemoteConfig
import FirebaseStorage

/// The iOS counterpart of the Hilt graph: what Android spreads over `AppModule`, `DatabaseModule`,
/// `NetworkModule`, `FirebaseModule` and `ScreensModule` is built here by hand.
///
/// Stored properties are the `@Singleton`s; computed ones are made anew for whoever asks, as
/// Hilt does with an unscoped class.
///
/// `FirebaseApp.configure()` has to run before this is created.
@MainActor
final class AppContainer {

    /// DatabaseModule
    let preferences: Preferences
    let matchLocal: MatchLocal
    let playerLocal: PlayerLocal
    let seasonLocal: SeasonLocal
    let teamLocal: TeamLocal

    /// NetworkModule + FirebaseModule
    let appVersion: AppVersionRemote
    let matchRemote: MatchRemote
    let playerRemote: PlayerRemote
    let seasonRemote: SeasonRemote
    let storage: StorageRemote
    let teamRemote: TeamRemote
    let usersRemote: UsersRemote
    let authRepository: AuthRepository

    /// Domain
    let sessionManager: SessionManager

    /// ScreensModule + AppModule
    let splashController: SplashController
    let initAppHandler: InitAppHandler
    let mainViewModel: MainViewModel

    init() {
        let database: SQLiteDatabase
        do {
            database = try GazteluDatabase.open()
        } catch {
            /// Nothing works without the cache, and there is nobody to recover for this early.
            fatalError("Could not open the local database: \(error.localizedDescription)")
        }

        preferences = PreferencesImpl()
        matchLocal = SQLiteMatches(database: database)
        playerLocal = SQLitePlayers(database: database)
        seasonLocal = SQLiteSeasons(database: database)
        teamLocal = SQLiteTeam(database: database)

        let firestore = Firestore.firestore()
        appVersion = RemoteConfigAppVersion(
            remoteConfigManager: RemoteConfigManager(remoteConfig: RemoteConfig.remoteConfig())
        )
        matchRemote = FirestoreMatches(firestore: firestore)
        playerRemote = FirestorePlayers(firestore: firestore)
        seasonRemote = FirestoreSeason(firestore: firestore)
        storage = FirebaseStorageRemote(
            fireStorage: Storage.storage(),
            imageCompressor: ImageCompressor()
        )
        teamRemote = FirestoreTeam(firestore: firestore)
        usersRemote = FirebaseUsers(firestore: firestore)
        authRepository = AuthRepositoryImpl(googleSignIn: IosGoogleSignIn())

        sessionManager = SessionManager(auth: authRepository, users: usersRemote)

        splashController = SplashState()
        initAppHandler = FirstTime(
            appVersion: appVersion,
            matchLocal: matchLocal,
            matchRemote: matchRemote,
            preferences: preferences,
            playerLocal: playerLocal,
            playerRemote: playerRemote,
            seasonLocal: seasonLocal,
            seasonRemote: seasonRemote,
            teamLocal: teamLocal,
            teamRemote: teamRemote
        )
        mainViewModel = MainViewModel(
            sessionManager: sessionManager,
            splashController: splashController,
            initAppHandler: initAppHandler
        )
    }

    /// Auth use cases
    var loginEmail: LoginEmail {
        LoginEmail(repository: authRepository)
    }

    var loginGoogle: LoginGoogle {
        LoginGoogle(
            repository: authRepository,
            isUserInserted: IsUserInserted(repository: usersRemote),
            insertUser: InsertUser(repository: usersRemote)
        )
    }

    var logout: Logout {
        Logout(sessionManager: sessionManager)
    }

    var signUpEmail: SignUpEmail {
        SignUpEmail(repository: authRepository, insertUser: InsertUser(repository: usersRemote))
    }

    /// Use cases
    var selectedSeason: SelectedSeason {
        SelectedSeason(seasons: seasonLocal, preferences: preferences)
    }

    var getAvailableDorsals: GetAvailableDorsals {
        GetAvailableDorsals(selectedSeason: selectedSeason, seasonLocal: seasonLocal)
    }

    var getMatchDetail: GetMatchDetail {
        GetMatchDetail(matchLocal: matchLocal)
    }

    var getMatchPlayerStats: GetMatchPlayerStats {
        GetMatchPlayerStats(playerLocal: playerLocal)
    }

    var getMatches: GetMatches {
        GetMatches(selectedSeason: selectedSeason, matchLocal: matchLocal)
    }

    var getPlayer: GetPlayer {
        GetPlayer(playerLocal: playerLocal)
    }

    var getPlayerStats: GetPlayerStats {
        GetPlayerStats(selectedSeason: selectedSeason, playerLocal: playerLocal)
    }

    var getPlayers: GetPlayers {
        GetPlayers(playerLocal: playerLocal)
    }

    var getSeasonPlayer: GetSeasonPlayer {
        GetSeasonPlayer(seasonLocal: seasonLocal)
    }

    var getSeasonPlayerStats: GetSeasonPlayerStats {
        GetSeasonPlayerStats(playerLocal: playerLocal)
    }

    var getSquad: GetSquad {
        GetSquad(seasons: seasonLocal)
    }

    var getTeam: GetTeam {
        GetTeam(teams: teamLocal)
    }

    var getTeams: GetTeams {
        GetTeams(teams: teamLocal)
    }

    var insertPlayer: InsertPlayer {
        InsertPlayer(
            storage: storage,
            selectedSeason: selectedSeason,
            playerLocal: playerLocal,
            playerRemote: playerRemote,
            seasonLocal: seasonLocal
        )
    }

    var insertTeam: InsertTeam {
        InsertTeam(
            storage: storage,
            selectedSeason: selectedSeason,
            teamLocal: teamLocal,
            teamRemote: teamRemote
        )
    }
}
