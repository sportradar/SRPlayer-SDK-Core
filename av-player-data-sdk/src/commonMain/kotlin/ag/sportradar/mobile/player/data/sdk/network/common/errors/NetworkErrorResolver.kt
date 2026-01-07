package ag.sportradar.mobile.player.data.sdk.network.common.errors

import io.ktor.client.statement.HttpResponse

/**
 * A functional interface for resolving network errors from HTTP responses.
 * Implementations of this interface are responsible for mapping HTTP responses to specific [HttpError] instances.
 */
internal fun interface NetworkErrorResolver {
    /**
     * Resolves the given [response] to an [HttpError] if applicable, or throws [IllegalStateException] if the response cannot be mapped to a [HttpError].
     *
     * @param response The HTTP response to resolve.
     * @return The resolved [HttpError], or an [IllegalStateException] if the response cannot be mapped to a [HttpError].
     */
    suspend fun resolve(response: HttpResponse): HttpError
}