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

/// The iOS counterparts of Android's response classes and network mappers for everything that is
/// not a match. The ids never come from the document: they are in its path.

enum PlayerMapper {

    static func asModel(_ data: DocumentData, id: PlayerId) -> Player {
        Player(
            id: id,
            name: data.string("name") ?? "",
            nickname: data.string("nickname"),
            faceImage: data.string("faceImage"),
            bodyImage: data.string("bodyImage")
        )
    }

    static func asResponse(_ player: Player) -> DocumentData {
        [
            "name": player.name,
            "nickname": orNull(player.nickname),
            "faceImage": orNull(player.faceImage),
            "bodyImage": orNull(player.bodyImage),
            "updatedAt": FieldValue.serverTimestamp()
        ]
    }
}

enum SeasonMapper {

    static func asModel(_ data: DocumentData, id: SeasonId) -> Season {
        let now = Int(Date().timeIntervalSince1970)
        return Season(
            id: id,
            name: data.string("name") ?? "",
            isCurrent: data.bool("current") ?? false,
            startsAt: data.seconds("startsAt") ?? now,
            endsAt: data.seconds("endsAt") ?? now
        )
    }
}

/// A squad row is identified by two ids, the season it belongs to and the player it is about, and
/// both come from the path rather than from the document.
enum SeasonPlayerMapper {

    static func asModel(_ data: DocumentData, seasonId: SeasonId, playerId: PlayerId) -> SeasonPlayer {
        SeasonPlayer(
            id: playerId,
            seasonId: seasonId,
            dorsal: data.int("dorsal"),
            position: Position.mapPosition(data.string("position") ?? "")
        )
    }

    static func asResponse(_ player: SeasonPlayer) -> DocumentData {
        [
            "playerId": player.id.value,
            "dorsal": orNull(player.dorsal),
            "position": player.position.rawValue,
            "updatedAt": FieldValue.serverTimestamp()
        ]
    }
}

enum TeamMapper {

    static func asModel(_ data: DocumentData, id: TeamId, seasonId: SeasonId) -> Team {
        Team(
            id: id,
            seasonId: seasonId,
            name: data.string("name") ?? "",
            logo: data.string("logo") ?? ""
        )
    }

    static func asResponse(_ team: Team) -> DocumentData {
        [
            "logo": team.logo,
            "name": team.name,
            "updatedAt": FieldValue.serverTimestamp()
        ]
    }
}

enum UserMapper {

    static func asModel(_ data: DocumentData, id: UserId) -> User {
        User(
            uid: id,
            name: data.string("name") ?? "",
            email: data.string("email") ?? "",
            img: data.string("img")
        )
    }

    static func asResponse(_ user: User) -> DocumentData {
        [
            "id": user.uid.value,
            "name": user.name ?? "",
            "email": user.email ?? "",
            "img": orNull(user.img)
        ]
    }
}
