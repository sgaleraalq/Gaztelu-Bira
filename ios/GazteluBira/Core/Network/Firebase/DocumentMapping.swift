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

extension QuerySnapshot {

    /// Maps each document on its own: one that cannot be read is logged and left out instead of
    /// failing the whole read — and with it the sync that asked for it.
    func mapDocuments<T>(_ map: (QueryDocumentSnapshot) throws -> T?) -> [T] {
        documents.compactMap { document in
            do {
                return try map(document)
            } catch {
                Log.w("Skipped malformed document \(document.reference.path)", error)
                return nil
            }
        }
    }
}

/// A Firestore document as it arrives. Android lets Firestore fill a response class and falls back
/// on the defaults of its constructor; these readers are that fallback, field by field.
typealias DocumentData = [String: Any]

extension Dictionary where Key == String, Value == Any {

    func string(_ key: String) -> String? {
        self[key] as? String
    }

    func int(_ key: String) -> Int? {
        (self[key] as? NSNumber)?.intValue
    }

    func double(_ key: String) -> Double? {
        (self[key] as? NSNumber)?.doubleValue
    }

    func bool(_ key: String) -> Bool? {
        self[key] as? Bool
    }

    func dictionary(_ key: String) -> DocumentData {
        self[key] as? DocumentData ?? [:]
    }

    /// Seconds since 1970.
    func seconds(_ key: String) -> Int? {
        (self[key] as? Timestamp).map { Int($0.seconds) }
    }
}

/// Firestore drops a key whose value is `nil`; Android writes it as an explicit null instead, and
/// this keeps the documents written from iOS the same shape.
func orNull(_ value: Any?) -> Any {
    value ?? NSNull()
}
