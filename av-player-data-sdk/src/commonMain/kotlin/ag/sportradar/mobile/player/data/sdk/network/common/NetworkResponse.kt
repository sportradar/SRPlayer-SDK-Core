package ag.sportradar.mobile.player.data.sdk.network.common

import ag.sportradar.mobile.player.data.sdk.network.common.errors.HttpError

/**
 * Represents a network response, which can either be a success with [data] or a failure with an [error].
 * @param T The type of data expected in a successful response.
 * @property data The data returned in a successful response, or null if the response was a failure.
 * @property error The error that occurred during the network request, or null if the request was successful.
 */
internal data class NetworkResponse<T>(
    private val data: T? = null,
    private val error: HttpError? = null,
) {
    companion object {
        /**
         * Creates a successful [NetworkResponse] with the given [data].
         *
         * @param data The data to wrap in a successful response.
         * @return A successful [NetworkResponse] containing the provided data.
         */
        fun <T> success(data: T) = NetworkResponse(data = data)

        /**
         * Creates a failed [NetworkResponse] with the given[error].
         *
         * @param error The error to wrap in a failed response.
         * @return A failed [NetworkResponse] containing the provided error.
         */
        fun <T> failure(error: HttpError) = NetworkResponse<T>(error = error)
    }

    /**
     * Unwraps the [NetworkResponse] and executes either [onSuccess] with the [data] if successful, or [onFailure] with the [error] if failed.
     *
     * @param onSuccess The function to execute with the data if the response is successful.
     * @param onFailure The function to execute with the error if the response is a failure.
     * @return The result of either [onSuccess] or [onFailure].
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified R> unwrap(
        onSuccess: (T) -> R,
        onFailure: (HttpError) -> R,
    ): R =
        if (error != null) {
            onFailure(error)
        } else {
            onSuccess(data as T)
        }

    /**
     * Unwraps the [NetworkResponse] and executes either [onSuccess] with the [data] if successful, or [onFailure] with the [error] if failed.
     * This function is a suspending version of [unwrap].
     *
     * @param onSuccess The suspending function to execute with the data if the response is successful.
     * @param onFailure The suspending function to execute with the error if the response is a failure.
     * @return The result of either [onSuccess] or [onFailure].
     */
    @Suppress("UNCHECKED_CAST")
    suspend inline fun <reified R> unwrapSuspended(
        onSuccess: suspend (T) -> R,
        onFailure: suspend (HttpError) -> R,
    ): R =
        if (error != null) {
            onFailure(error)
        } else {
            onSuccess(data as T)
        }

    /**
     * Returns the data if the response is successful, or null if it's a failure.
     *
     * @return The data or null.
     */
    fun getOrNull(): T? = data

    /**
     * Returns the error if the response is a failure, or null if it's successful.
     *
     * @return The error or null.
     */
    fun errorOrNull(): HttpError? = error
}