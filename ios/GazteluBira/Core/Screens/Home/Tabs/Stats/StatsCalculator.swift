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

struct RankedPlayer {
    let player: SquadPlayer
    let value: Double
    let changedPosition: Int
}

struct SeasonStats {
    let squad: [SquadPlayer]
    let stats: [PlayerId: [PlayerStats]]
    let lastMatchId: MatchId?

    static let empty = SeasonStats(squad: [], stats: [:], lastMatchId: nil)
}

enum StatsCalculator {

    /// `RankedPlayer.changedPosition` compares against the ranking before the season's last match.
    static func rank(
        _ season: SeasonStats,
        stat: GBStat,
        punctuation: GBPunctuation
    ) -> [RankedPlayer] {
        let current = ranking(season, stat: stat, punctuation: punctuation) { _ in true }
        var previousPositions: [PlayerId: Int] = [:]
        if let last = season.lastMatchId {
            let previous = ranking(season, stat: stat, punctuation: punctuation) { $0.matchId != last }
            for (index, entry) in previous.enumerated() {
                previousPositions[entry.player.player.id] = index
            }
        }

        return current.enumerated().map { index, entry in
            let previousPosition = previousPositions[entry.player.player.id] ?? index
            return RankedPlayer(
                player: entry.player,
                value: entry.value,
                changedPosition: previousPosition - index
            )
        }
    }

    static func total(
        _ stats: [PlayerStats],
        stat: GBStat,
        punctuation: GBPunctuation
    ) -> Double {
        switch stat {
        case .percentage: return percentage(stats, punctuation: punctuation)
        case .goals: return Double(stats.sum { $0.goals })
        case .goalsProvoked: return Double(stats.sum { $0.goalsProvoked })
        case .assists: return Double(stats.sum { $0.assists })
        case .cleanSheets: return stats.reduce(0) { $0 + $1.cleanSheets }
        case .penaltiesProvoked: return Double(stats.sum { $0.penaltiesProvoked })
        case .saves: return Double(stats.sum { $0.saves })
        case .fails: return Double(stats.sum { $0.fails })
        case .yellowCards: return Double(stats.sum { $0.yellowCards })
        case .redCards: return Double(stats.sum { $0.redCards })
        case .blueCards: return Double(stats.sum { $0.blueCards })
        case .gamesPlayed: return Double(stats.count)
        default: return 0
        }
    }

    private static func ranking(
        _ season: SeasonStats,
        stat: GBStat,
        punctuation: GBPunctuation,
        counts: (PlayerStats) -> Bool
    ) -> [(player: SquadPlayer, value: Double)] {
        season.squad
            .filter { $0.position != .manager }
            .map { player in
                let playerStats = (season.stats[player.player.id] ?? []).filter(counts)
                return (player: player, value: total(playerStats, stat: stat, punctuation: punctuation))
            }
            .sorted { first, second in
                if first.value != second.value { return first.value > second.value }
                return first.player.player.name < second.player.player.name
            }
    }

    private static func percentage(_ stats: [PlayerStats], punctuation: GBPunctuation) -> Double {
        if stats.isEmpty { return 0 }

        let goals = Int(punctuation.goals) * stats.sum { $0.goals + $0.goalsProvoked }
        let assists = Int(punctuation.assists) * stats.sum { $0.assists }
        let penalties = Int(punctuation.penaltiesProvoked) * stats.sum { $0.penaltiesProvoked }
        let cleanSheets = Double(punctuation.cleanSheets) * stats.reduce(0) { $0 + $1.cleanSheets }
        let saves = Int(punctuation.saves) * stats.sum { $0.saves }
        let redCards = -wholeDivision(stats.sum { $0.redCards }, by: Int(punctuation.minRedCards))
        let yellowCards = -wholeDivision(stats.sum { $0.yellowCards }, by: Int(punctuation.minYellowCards))
        let fails = Int(punctuation.fails) * stats.sum { $0.fails }

        let points = Double(goals + assists + penalties + saves + redCards + yellowCards + fails) + cleanSheets
        return points / Double(stats.count)
    }

    /// An integer division, as on Android. Dividing by zero would crash, so a threshold of zero
    /// cards costs nothing.
    private static func wholeDivision(_ value: Int, by divisor: Int) -> Int {
        divisor == 0 ? 0 : value / divisor
    }
}

private extension Array where Element == PlayerStats {
    func sum(_ value: (PlayerStats) -> Int) -> Int {
        reduce(0) { $0 + value($1) }
    }
}
