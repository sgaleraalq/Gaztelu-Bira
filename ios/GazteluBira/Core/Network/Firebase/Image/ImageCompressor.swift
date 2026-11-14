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
import ImageIO
import UIKit

struct ImageCompressionError: Error, LocalizedError {
    let message: String

    var errorDescription: String? { message }
}

/// Converts any image picked by the user into a small `ImageFormat` file, ready to be uploaded.
final class ImageCompressor {

    /// - Parameters:
    ///   - url: File url of the source image (any format iOS can decode).
    ///   - maxSize: Max size in pixels of the longest side of the result.
    ///   - format: Output format. With `.jpeg` transparent areas are filled with white, since JPEG
    ///     has no alpha channel; `.png` keeps them.
    func compress(url: URL, maxSize: Int, format: ImageFormat) async throws -> Data {
        try await Task.detached(priority: .userInitiated) {
            let source = try Self.decodeSampled(url: url, maxSize: maxSize)
            let image = UIImage(cgImage: source)
            let data = format.supportsAlpha
                ? image.pngData()
                : Self.onWhiteBackground(image).jpegData(compressionQuality: format.quality)
            guard let data else { throw ImageCompressionError(message: "Could not encode image") }
            return data
        }.value
    }

    /// Decodes the image already downsampled, so big photos never get fully loaded into memory.
    /// ImageIO applies the EXIF rotation on the way, and never scales a small image up.
    private static func decodeSampled(url: URL, maxSize: Int) throws -> CGImage {
        let sourceOptions: [CFString: Any] = [kCGImageSourceShouldCache: false]
        guard let source = CGImageSourceCreateWithURL(url as CFURL, sourceOptions as CFDictionary) else {
            throw ImageCompressionError(message: "Could not open image")
        }
        let options: [CFString: Any] = [
            kCGImageSourceCreateThumbnailFromImageAlways: true,
            kCGImageSourceCreateThumbnailWithTransform: true,
            kCGImageSourceShouldCacheImmediately: true,
            kCGImageSourceThumbnailMaxPixelSize: maxSize
        ]
        guard let image = CGImageSourceCreateThumbnailAtIndex(source, 0, options as CFDictionary) else {
            throw ImageCompressionError(message: "Unsupported image format")
        }
        return image
    }

    private static func onWhiteBackground(_ image: UIImage) -> UIImage {
        let format = UIGraphicsImageRendererFormat()
        format.scale = 1
        format.opaque = true
        return UIGraphicsImageRenderer(size: image.size, format: format).image { context in
            UIColor.white.setFill()
            context.fill(CGRect(origin: .zero, size: image.size))
            image.draw(at: .zero)
        }
    }
}
