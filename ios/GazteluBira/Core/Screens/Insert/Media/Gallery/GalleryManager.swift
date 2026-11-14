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

import PhotosUI
import SwiftUI
import UIKit

extension View {

    /// The iOS counterpart of Android's `rememberGalleryManager`: shows the system photo picker
    /// while `isPresented` is true and hands back the picture the user chose.
    ///
    /// There is no permission to ask for, and so nothing like `onPermissionDenied`: the picker runs
    /// outside the app and only the chosen picture is shared with it.
    ///
    /// - Parameter onImageSelected: Receives the url of a local copy of the picture.
    func galleryPicker(
        isPresented: Binding<Bool>,
        onImageSelected: @escaping (URL) -> Void
    ) -> some View {
        modifier(GalleryPicker(isPresented: isPresented, onImageSelected: onImageSelected))
    }
}

private struct GalleryPicker: ViewModifier {

    @Binding var isPresented: Bool
    let onImageSelected: (URL) -> Void

    @State private var selection: PhotosPickerItem?

    func body(content: Content) -> some View {
        content
            .photosPicker(isPresented: $isPresented, selection: $selection, matching: .images)
            .onChange(of: selection) { item in
                guard let item else { return }
                /// Cleared so picking the same picture twice in a row still counts as a change.
                selection = nil
                Task {
                    guard let data = try? await item.loadTransferable(type: Data.self),
                          let url = await Gallery.store(data) else {
                        Log.w("The picked image could not be read")
                        return
                    }
                    onImageSelected(url)
                }
            }
    }
}

enum Gallery {

    /// Big enough for the preview and for every size the app uploads.
    private static let maxSize: CGFloat = 2048

    /// Saves the picked picture where the shared UI can load it from, and where the upload reads
    /// it back.
    ///
    /// Photos hands pictures over as they are stored, usually HEIC and rotated through metadata,
    /// and the image loader of the shared UI is not guaranteed to understand either. So the copy
    /// is redrawn upright and saved as PNG when it has transparency (a player's cut-out body) and
    /// as JPEG otherwise.
    static func store(_ data: Data) async -> URL? {
        await Task.detached(priority: .userInitiated) { () -> URL? in
            guard let image = UIImage(data: data) else { return nil }

            let scale = min(1, maxSize / max(image.size.width, image.size.height))
            let size = CGSize(width: image.size.width * scale, height: image.size.height * scale)
            let hasAlpha = image.hasAlpha

            let format = UIGraphicsImageRendererFormat()
            format.scale = 1
            format.opaque = !hasAlpha
            let upright = UIGraphicsImageRenderer(size: size, format: format).image { _ in
                image.draw(in: CGRect(origin: .zero, size: size))
            }

            let encoded = hasAlpha ? upright.pngData() : upright.jpegData(compressionQuality: 0.9)
            guard let encoded else { return nil }

            let url = FileManager.default.temporaryDirectory
                .appendingPathComponent(UUID().uuidString)
                .appendingPathExtension(hasAlpha ? "png" : "jpg")
            do {
                try encoded.write(to: url, options: .atomic)
                return url
            } catch {
                Log.w("The picked image could not be saved", error)
                return nil
            }
        }.value
    }
}

private extension UIImage {
    var hasAlpha: Bool {
        guard let alpha = cgImage?.alphaInfo else { return false }
        switch alpha {
        case .none, .noneSkipFirst, .noneSkipLast: return false
        default: return true
        }
    }
}
