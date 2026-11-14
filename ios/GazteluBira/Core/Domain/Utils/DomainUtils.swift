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

enum GazteluBiraUtils {

    static let gazteluBiraId = "gaztelu_bira"
    static let gazteluBiraName = "Gaztelu Bira"
    static let gazteluBiraLogo =
        "https://firebasestorage.googleapis.com/v0/b/gbmultiplatform.firebasestorage.app/o/" +
        "img_gaztelu_bira.webp?alt=media&token=3708f1c5-f9d7-4353-8829-967b21df75ed"

    static let gazteluBira = Team(
        id: TeamId(gazteluBiraId),
        seasonId: SeasonId(""),
        name: gazteluBiraName,
        logo: gazteluBiraLogo
    )
}

extension String {

    /// Builds a document id from a display name, the same way the seed does:
    /// "Adrián Orta" -> "adrian_orta", "Esperanza de Arazuri" -> "esperanza_de_arazuri".
    func toFirebaseId() -> String {
        let decomposed = trimmingCharacters(in: .whitespacesAndNewlines)
            .decomposedStringWithCanonicalMapping
        let withoutDiacritics = String(
            String.UnicodeScalarView(
                decomposed.unicodeScalars.filter { !CharacterSet.nonBaseCharacters.contains($0) }
            )
        )
        return withoutDiacritics
            .lowercased()
            .replacingOccurrences(of: "[^a-z0-9]+", with: "_", options: .regularExpression)
            .trimmingCharacters(in: CharacterSet(charactersIn: "_"))
    }
}

let dateFormat = "dd/MM/yyyy"

/// The iOS counterpart of `DecimalFormat("#.##")`: up to two decimals, none when there are none.
func formatDecimal(_ value: Double, maximumFractionDigits: Int = 2) -> String {
    let formatter = NumberFormatter()
    formatter.numberStyle = .decimal
    formatter.usesGroupingSeparator = false
    formatter.minimumFractionDigits = 0
    formatter.maximumFractionDigits = maximumFractionDigits
    formatter.roundingMode = .halfEven
    return formatter.string(from: NSNumber(value: value)) ?? String(value)
}

/// Milliseconds since 1970.
func getActualTimeAsLong() -> Int {
    Int(Date().timeIntervalSince1970 * 1_000)
}

/// - Parameter date: Milliseconds since 1970.
func getDateFromLong(format: String, date: Int) -> String {
    let formatter = DateFormatter()
    formatter.dateFormat = format
    formatter.timeZone = .current
    return formatter.string(from: Date(timeIntervalSince1970: TimeInterval(date) / 1_000))
}

extension Int {
    /// Reads the number as milliseconds since 1970.
    func toDate() -> String {
        getDateFromLong(format: dateFormat, date: self)
    }
}
