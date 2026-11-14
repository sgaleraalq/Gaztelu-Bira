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
import FirebaseStorage

private let teamLogoMaxSize = 256
private let playerFaceMaxSize = 256
private let playerBodyMaxSize = 512

private let faceFile = "face"
private let bodyFile = "body"

/// The iOS counterpart of Android's `FirebaseStorage` implementation of `StorageRemote`; the name
/// differs so it does not shadow the SDK's own types.
final class FirebaseStorageRemote: StorageRemote {

    private let fireStorage: Storage
    private let imageCompressor: ImageCompressor

    init(fireStorage: Storage, imageCompressor: ImageCompressor) {
        self.fireStorage = fireStorage
        self.imageCompressor = imageCompressor
    }

    func insertTeamLogo(_ id: TeamId, url: URL) async -> FirebaseInsertResult {
        await upload(
            path: "\(FirebaseConstants.teams)/\(id)",
            url: url,
            maxSize: teamLogoMaxSize,
            format: .jpeg
        )
    }

    /// Faces go as JPEG on white, bodies as PNG so their transparent background is kept.
    func insertPlayerImage(_ id: PlayerId, type: PictureType, url: URL) async -> FirebaseInsertResult {
        switch type {
        case .face:
            return await upload(
                path: "\(FirebaseConstants.players)/\(id)/\(faceFile)",
                url: url,
                maxSize: playerFaceMaxSize,
                format: .jpeg
            )
        case .body:
            return await upload(
                path: "\(FirebaseConstants.players)/\(id)/\(bodyFile)",
                url: url,
                maxSize: playerBodyMaxSize,
                format: .png
            )
        }
    }

    /// - Parameter path: Storage path without extension, the format adds it.
    private func upload(
        path: String,
        url: URL,
        maxSize: Int,
        format: ImageFormat
    ) async -> FirebaseInsertResult {
        do {
            let image = try await imageCompressor.compress(url: url, maxSize: maxSize, format: format)
            let metadata = StorageMetadata()
            metadata.contentType = format.mimeType
            let reference = fireStorage.reference().child("\(path).\(format.fileExtension)")
            _ = try await reference.putDataAsync(image, metadata: metadata)
            let downloadUrl = try await reference.downloadURL()
            return .image(url: downloadUrl)
        } catch {
            return .unknown(message: error.localizedDescription)
        }
    }
}
