package ag.sportradar.mobile.player.data.sdk.network.common

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

/**
 * Resolves the body of the [HttpResponse] to the specified type [T].
 * Handles the case where [T] is a String to prevent deserialization attempts.
 *
 * @return The resolved body of the response.
 */
internal suspend inline fun <reified T> HttpResponse.resolveBody(): T =
    if ("" is T) {
        // String is type T && bodyAsText() returns String => bodyAsText(): String as T should succeed
        this.bodyAsText() as T
    } else {
        // otherwise resolve normally
        this.body()
    }
