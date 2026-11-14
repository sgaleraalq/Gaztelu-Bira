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

/// Formats the app uploads to Storage.
enum ImageFormat {
    case jpeg
    case png

    var fileExtension: String {
        switch self {
        case .jpeg: return "jpg"
        case .png: return "png"
        }
    }

    var mimeType: String {
        switch self {
        case .jpeg: return "image/jpeg"
        case .png: return "image/png"
        }
    }

    /// Ignored by PNG, which is lossless.
    var quality: CGFloat {
        switch self {
        case .jpeg: return 0.85
        case .png: return 1
        }
    }

    var supportsAlpha: Bool {
        self == .png
    }
}
