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

extension MatchDetail {

    func toMatchDetailInformation(localTeam: GBTeam, visitorTeam: GBTeam) -> MatchDetailInformation {
        IosUiStatesKt.matchDetailInformation(
            local: localTeam,
            visitor: visitorTeam,
            date: Int64(match.information.date),
            text: match.information.description,
            location: match.information.location
        )
    }

    func toMatchDetailLineUp(_ players: [PlayerId: Player]) -> MatchDetailLineUp {
        var positions: [KotlinInt: GBPlayer] = [:]
        for player in self.players {
            guard let position = player.formationPosition else { continue }
            positions[(position - 1).kotlinBoxed] = player.playerId.toGBPlayer(players)
        }
        return MatchDetailLineUp(
            benchPlayers: toGBPlayers(.bench, players),
            managers: toGBPlayers(.manager, players),
            matchFormation: match.match.formation.toLineUpFormation(),
            players: positions
        )
    }

    private func toGBPlayers(_ state: MatchPlayerState, _ players: [PlayerId: Player]) -> [GBPlayer] {
        self.players
            .filter { $0.state == state }
            .map { $0.playerId.toGBPlayer(players) }
    }
}

extension Array where Element == PlayerStats {

    func toMatchDetailStats(_ players: [PlayerId: Player]) -> MatchDetailStats {
        func playersWith(_ stat: (PlayerStats) -> Double) -> [GBPlayer] {
            filter { stat($0) > 0 }.map { $0.playerId.toGBPlayer(players) }
        }
        return MatchDetailStats(
            assists: playersWith { Double($0.assists) },
            blueCards: playersWith { Double($0.blueCards) },
            cleanSheets: playersWith { $0.cleanSheets },
            fails: playersWith { Double($0.fails) },
            goals: playersWith { Double($0.goals) },
            goalsProvoked: playersWith { Double($0.goalsProvoked) },
            penaltiesProvoked: playersWith { Double($0.penaltiesProvoked) },
            redCards: playersWith { Double($0.redCards) },
            saves: playersWith { Double($0.saves) },
            yellowCards: playersWith { Double($0.yellowCards) }
        )
    }
}
