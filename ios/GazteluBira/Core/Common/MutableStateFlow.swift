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

/// The iOS counterpart of Kotlin's `MutableStateFlow`: it always holds a value, and whoever
/// listens gets the current one first and every change after it.
///
/// It exists so the layers below the screens read like their Android originals, which are built on
/// flows, without pulling Combine into the domain.
final class MutableStateFlow<Value>: @unchecked Sendable {

    private let lock = NSLock()
    private var current: Value
    private var observers: [UUID: AsyncStream<Value>.Continuation] = [:]

    init(_ value: Value) {
        current = value
    }

    var value: Value {
        get {
            lock.lock()
            defer { lock.unlock() }
            return current
        }
        set {
            /// Yielding never blocks, so it is done under the lock: observers see the values in the
            /// order they were set.
            lock.lock()
            current = newValue
            observers.values.forEach { $0.yield(newValue) }
            lock.unlock()
        }
    }

    func update(_ transform: (Value) -> Value) {
        lock.lock()
        current = transform(current)
        let updated = current
        observers.values.forEach { $0.yield(updated) }
        lock.unlock()
    }

    /// The current value, then every value set from now on. It ends when its consumer is cancelled.
    func stream() -> AsyncStream<Value> {
        AsyncStream { continuation in
            let id = UUID()
            lock.lock()
            observers[id] = continuation
            continuation.yield(current)
            lock.unlock()
            continuation.onTermination = { [weak self] _ in
                self?.remove(id)
            }
        }
    }

    private func remove(_ id: UUID) {
        lock.lock()
        observers[id] = nil
        lock.unlock()
    }
}

extension AsyncStream where Element: Equatable {

    /// The iOS counterpart of `distinctUntilChanged()`.
    func distinctUntilChanged() -> AsyncStream<Element> {
        AsyncStream { continuation in
            let task = Task {
                var last: Element?
                var started = false
                for await value in self {
                    if started && value == last { continue }
                    started = true
                    last = value
                    continuation.yield(value)
                }
                continuation.finish()
            }
            continuation.onTermination = { _ in task.cancel() }
        }
    }
}

extension AsyncStream {

    /// The iOS counterpart of `map { }` on a flow, with a transform that may suspend.
    func mapAsync<Output>(_ transform: @escaping (Element) async -> Output) -> AsyncStream<Output> {
        AsyncStream<Output> { continuation in
            let task = Task {
                for await value in self {
                    continuation.yield(await transform(value))
                }
                continuation.finish()
            }
            continuation.onTermination = { _ in task.cancel() }
        }
    }
}
