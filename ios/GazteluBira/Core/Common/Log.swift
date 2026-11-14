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
import os

/// The iOS counterpart of Android's `Log` plus the `TAG` helper of `:core:common`: the tag is the
/// name of the file the call is written in, so nobody has to declare one.
enum Log {

    private static let subsystem = Bundle.main.bundleIdentifier ?? "com.sgale.gaztelubira"

    static func w(_ message: String, _ error: Error? = nil, file: String = #fileID) {
        logger(file).warning("\(describe(message, error), privacy: .public)")
    }

    static func e(_ message: String, _ error: Error? = nil, file: String = #fileID) {
        logger(file).error("\(describe(message, error), privacy: .public)")
    }

    private static func logger(_ file: String) -> Logger {
        Logger(subsystem: subsystem, category: tag(file))
    }

    /// "GazteluBira/FirstTime.swift" -> "FirstTime"
    private static func tag(_ file: String) -> String {
        let name = file.split(separator: "/").last.map(String.init) ?? file
        return name.replacingOccurrences(of: ".swift", with: "")
    }

    private static func describe(_ message: String, _ error: Error?) -> String {
        guard let error else { return message }
        return "\(message): \(error.localizedDescription)"
    }
}
