package ag.sportradar.mobile.player.data.sdk.network.common.errors

import ag.sportradar.mobile.player.data.sdk.network.common.resolveBody
import io.github.aakira.napier.Napier
import io.ktor.client.statement.HttpResponse

/**
 * A [NetworkErrorResolver] implementation that attempts to parse the response body as an [HttpError] object.
 * If the parsing fails, it returns `null`.
 */
internal class DefaultErrorResolver : NetworkErrorResolver {
    /**
     * Resolves the given [response] to an [HttpError] by parsing the response body, or returns `null` if the parsing fails.
     *
     * @param response The HTTP response to resolve.
     * @return The resolved [HttpError] parsed from the response body, or `null` if parsing fails.
     */
    override suspend fun resolve(response: HttpResponse): HttpError =
        try {
            response.resolveBody<HttpError>()
        } catch (t: Throwable) {
            Napier.i("Unable to parse response body as 'HttpError': ${t.message}\n${t.cause}")
            error("Unable to parse response body as 'HttpError'")
        }
}
