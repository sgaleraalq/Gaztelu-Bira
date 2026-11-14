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
import GazteluBiraUI

@MainActor
final class StatsViewModel: ObservableObject {

    let state = IosState<StatsUiState>(initial: IosUiStatesKt.statsUiState())

    private let getSquad: GetSquad
    private let getSeasonPlayerStats: GetSeasonPlayerStats
    private let getMatches: GetMatches
    private let seasonLocal: SeasonLocal
    private let preferences: Preferences

    /// What the selected season brought. Nil until its first load: the screen stays on its loading
    /// state until then, as it does on Android before the flows emit.
    private var seasonStats: SeasonStats?
    private var selectedSeasonId: SeasonId?
    private var allSeasons: [StatsSeason] = []

    private var selectedStat: GBStat = .percentage
    private var punctuation: GBPunctuation = IosUiStatesKt.statsUiState().punctuation

    /// StatsControls
    private var screenState: GBStatsState = IosSealedStatesKt.statsStateDefault()
    private var settings: GBStatsSettings = IosSealedStatesKt.statsSettingsHidden()
    private var selectedPlayer: PlayerId?

    private var observation: Task<Void, Never>?

    init(
        selectedSeason: SelectedSeason,
        getSquad: GetSquad,
        getSeasonPlayerStats: GetSeasonPlayerStats,
        getMatches: GetMatches,
        seasonLocal: SeasonLocal,
        preferences: Preferences
    ) {
        self.getSquad = getSquad
        self.getSeasonPlayerStats = getSeasonPlayerStats
        self.getMatches = getMatches
        self.seasonLocal = seasonLocal
        self.preferences = preferences

        let seasons = selectedSeason()
        observation = Task { [weak self] in
            for await season in seasons {
                await self?.load(season)
            }
        }
    }

    deinit {
        observation?.cancel()
    }

    func onChangeState(_ newState: GBStatsState) {
        screenState = newState
        render()
    }

    func onChangeSeason(_ newSeason: String) {
        onChangeState(IosSealedStatesKt.statsStateDefault())
        Task { [preferences] in
            await preferences.selectSeason(SeasonId(newSeason))
        }
    }

    func onStatSelected(_ stat: GBStat) {
        selectedStat = stat
        render()
    }

    func onPunctuationDraftChanged(_ punctuation: GBPunctuation) {
        settings = IosSealedStatesKt.statsSettingsChangePunctuation(draft: punctuation)
        render()
    }

    /// Only now does the leaderboard get rescored: the sliders were editing a draft.
    func onPunctuationConfirmed(_ punctuation: GBPunctuation) {
        self.punctuation = punctuation
        render()
    }

    func onSettingsChanged(_ settings: GBStatsSettings) {
        self.settings = settings
        render()
    }

    func onShowPlayer(_ id: String) {
        selectedPlayer = PlayerId(id)
        render()
    }

    func onDismissPlayer() {
        selectedPlayer = nil
        render()
    }

    private func load(_ season: SeasonId?) async {
        allSeasons = ((try? await seasonLocal.getSeasons()) ?? [])
            .map { StatsSeason(id: $0.id.value, name: $0.name) }
        selectedSeasonId = season
        if let season {
            seasonStats = await loadSeasonStats(season)
        } else {
            seasonStats = .empty
        }
        render()
    }

    private func loadSeasonStats(_ seasonId: SeasonId) async -> SeasonStats {
        let squad = (try? await getSquad(seasonId)) ?? []
        let stats = (try? await getSeasonPlayerStats(seasonId)) ?? []
        let matches = (try? await getMatches(seasonId)) ?? []
        return SeasonStats(
            squad: squad,
            stats: Dictionary(grouping: stats) { $0.playerId },
            lastMatchId: matches.max { $0.information.date < $1.information.date }?.id
        )
    }

    private func render() {
        guard let season = seasonStats else { return }

        let selected = allSeasons.first { $0.id == selectedSeasonId?.value }
        let ranked = StatsCalculator.rank(season, stat: selectedStat, punctuation: punctuation)

        state.value = StatsUiState(
            state: screenState,
            selectedStat: selectedStat,
            players: ranked.map { $0.toGBPlayerStat() },
            punctuation: punctuation,
            selectedPlayer: selectedPlayer.flatMap { season.toDetail($0, punctuation: punctuation) },
            seasons: selected.map { IosUiStatesKt.statsSeasons(selectedSeason: $0, seasons: allSeasons) }
                ?? IosUiStatesKt.emptyStatsSeasons(),
            settings: settings
        )
    }
}

/// StatsUiMapper
private extension RankedPlayer {
    func toGBPlayerStat() -> GBPlayerStat {
        GBPlayerStat(
            id: player.player.id.value,
            name: player.player.displayName(),
            dorsal: player.dorsal?.kotlinBoxed,
            faceImage: player.player.faceImage,
            bodyImage: player.player.bodyImage,
            position: player.position.toGBPosition(),
            value: formatDecimal(value),
            changedPosition: changedPosition.kotlin
        )
    }
}

private extension SeasonStats {

    /// Every stat but the blue cards, in the order the shared `GBStat` declares them.
    private static let detailStats: [GBStat] = [
        .percentage,
        .goals,
        .goalsProvoked,
        .assists,
        .cleanSheets,
        .penaltiesProvoked,
        .saves,
        .fails,
        .yellowCards,
        .redCards,
        .gamesPlayed
    ]

    func toDetail(_ id: PlayerId, punctuation: GBPunctuation) -> GBPlayerStatsDetail? {
        guard let player = squad.first(where: { $0.player.id == id })?.player else { return nil }
        let playerStats = stats[id] ?? []
        return GBPlayerStatsDetail(
            name: player.displayName(),
            faceImage: player.faceImage,
            stats: SeasonStats.detailStats.map { stat in
                let value = formatDecimal(
                    StatsCalculator.total(playerStats, stat: stat, punctuation: punctuation)
                )
                return GBStatValue(stat: stat, value: stat == .percentage ? "\(value) %" : value)
            }
        )
    }
}
