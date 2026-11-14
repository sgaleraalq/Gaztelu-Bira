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
import SQLite3

struct DatabaseError: Error, LocalizedError {
    let message: String

    var errorDescription: String? { message }
}

enum SQLiteValue {
    case null
    case integer(Int)
    case real(Double)
    case text(String)

    static func optional(_ value: Int?) -> SQLiteValue {
        value.map { .integer($0) } ?? .null
    }

    static func optional(_ value: String?) -> SQLiteValue {
        value.map { .text($0) } ?? .null
    }

    static func optional(_ value: Bool?) -> SQLiteValue {
        value.map { .bool($0) } ?? .null
    }

    static func bool(_ value: Bool) -> SQLiteValue {
        .integer(value ? 1 : 0)
    }
}

/// One row of a result, read by column name the way Room fills an entity.
struct SQLiteRow {

    fileprivate let statement: OpaquePointer
    fileprivate let columns: [String: Int32]

    func string(_ column: String) throws -> String {
        guard let value = try optionalString(column) else {
            throw DatabaseError(message: "Column \(column) is null")
        }
        return value
    }

    func optionalString(_ column: String) throws -> String? {
        let index = try index(of: column)
        guard sqlite3_column_type(statement, index) != SQLITE_NULL,
              let text = sqlite3_column_text(statement, index) else { return nil }
        return String(cString: text)
    }

    func int(_ column: String) throws -> Int {
        Int(sqlite3_column_int64(statement, try index(of: column)))
    }

    func optionalInt(_ column: String) throws -> Int? {
        let index = try index(of: column)
        guard sqlite3_column_type(statement, index) != SQLITE_NULL else { return nil }
        return Int(sqlite3_column_int64(statement, index))
    }

    func double(_ column: String) throws -> Double {
        sqlite3_column_double(statement, try index(of: column))
    }

    func bool(_ column: String) throws -> Bool {
        try int(column) != 0
    }

    func optionalBool(_ column: String) throws -> Bool? {
        try optionalInt(column).map { $0 != 0 }
    }

    private func index(of column: String) throws -> Int32 {
        guard let index = columns[column] else {
            throw DatabaseError(message: "No column named \(column)")
        }
        return index
    }
}

/// A SQLite connection. It is not thread safe on its own: `SQLiteDatabase` is what keeps every use
/// of it on one queue.
final class SQLiteConnection {

    /// Tells SQLite to copy the bound text, since the Swift string does not outlive the call.
    private static let transient = unsafeBitCast(-1, to: sqlite3_destructor_type.self)

    private var handle: OpaquePointer?

    init(path: String) throws {
        guard sqlite3_open(path, &handle) == SQLITE_OK else {
            let message = handle.map { String(cString: sqlite3_errmsg($0)) } ?? "unknown"
            sqlite3_close(handle)
            throw DatabaseError(message: "Could not open database: \(message)")
        }
    }

    deinit {
        sqlite3_close(handle)
    }

    func execute(_ sql: String, _ arguments: [SQLiteValue] = []) throws {
        let statement = try prepare(sql, arguments)
        defer { sqlite3_finalize(statement) }
        var result = sqlite3_step(statement)
        while result == SQLITE_ROW {
            result = sqlite3_step(statement)
        }
        guard result == SQLITE_DONE else { throw lastError(sql) }
    }

    func query<T>(
        _ sql: String,
        _ arguments: [SQLiteValue] = [],
        map: (SQLiteRow) throws -> T
    ) throws -> [T] {
        let statement = try prepare(sql, arguments)
        defer { sqlite3_finalize(statement) }

        var columns: [String: Int32] = [:]
        for index in 0..<sqlite3_column_count(statement) {
            columns[String(cString: sqlite3_column_name(statement, index))] = index
        }
        let row = SQLiteRow(statement: statement, columns: columns)

        var rows: [T] = []
        while true {
            let result = sqlite3_step(statement)
            if result == SQLITE_DONE { break }
            guard result == SQLITE_ROW else { throw lastError(sql) }
            rows.append(try map(row))
        }
        return rows
    }

    /// The iOS counterpart of Room's `@Transaction`: everything in `work` lands, or nothing does.
    func transaction(_ work: () throws -> Void) throws {
        try execute("BEGIN IMMEDIATE")
        do {
            try work()
            try execute("COMMIT")
        } catch {
            try? execute("ROLLBACK")
            throw error
        }
    }

    private func prepare(_ sql: String, _ arguments: [SQLiteValue]) throws -> OpaquePointer {
        var statement: OpaquePointer?
        guard sqlite3_prepare_v2(handle, sql, -1, &statement, nil) == SQLITE_OK, let statement else {
            throw lastError(sql)
        }
        for (offset, argument) in arguments.enumerated() {
            let index = Int32(offset + 1)
            let result: Int32
            switch argument {
            case .null:
                result = sqlite3_bind_null(statement, index)
            case .integer(let value):
                result = sqlite3_bind_int64(statement, index, Int64(value))
            case .real(let value):
                result = sqlite3_bind_double(statement, index, value)
            case .text(let value):
                result = sqlite3_bind_text(statement, index, value, -1, SQLiteConnection.transient)
            }
            guard result == SQLITE_OK else {
                sqlite3_finalize(statement)
                throw lastError(sql)
            }
        }
        return statement
    }

    private func lastError(_ sql: String) -> DatabaseError {
        let message = handle.map { String(cString: sqlite3_errmsg($0)) } ?? "unknown"
        return DatabaseError(message: "\(message) [\(sql)]")
    }
}

/// Runs every query on one background queue, so the callers can await the database from the main
/// thread the way Android awaits Room's suspend functions.
final class SQLiteDatabase {

    private let connection: SQLiteConnection
    private let queue = DispatchQueue(label: "com.sgale.gaztelubira.database")

    init(connection: SQLiteConnection) {
        self.connection = connection
    }

    func run<T>(_ work: @escaping (SQLiteConnection) throws -> T) async throws -> T {
        try await withCheckedThrowingContinuation { continuation in
            queue.async { [connection] in
                continuation.resume(with: Result { try work(connection) })
            }
        }
    }
}
