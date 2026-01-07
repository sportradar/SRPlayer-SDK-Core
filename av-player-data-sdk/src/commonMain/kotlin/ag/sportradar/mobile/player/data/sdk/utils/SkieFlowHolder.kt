package ag.sportradar.mobile.player.data.sdk.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A simple holder for a [StateFlow] that allows updating its value.
 *
 * This class wraps a [MutableStateFlow] and exposes it as an immutable [StateFlow].
 * It provides an [update] method to change the value, making it useful for managing observable state in a
 * platform-agnostic way (e.g., for UI or business logic).
 *
 * @param T The type of value held by the flow. Must be non-nullable.
 * @property stateFlow The observable state as a [StateFlow].
 * @constructor Initializes the holder with the given initial value.
 */
class SkieFlowHolder<T: Any>(initValue: T) {
    private val _stateFlow = MutableStateFlow(initValue)
    val stateFlow: StateFlow<T> = _stateFlow.asStateFlow()

    /**
     * Updates the value of the state flow.
     *
     * @param value The new value to set.
     */
    fun update(value: T) {
        _stateFlow.value = value
    }
}

