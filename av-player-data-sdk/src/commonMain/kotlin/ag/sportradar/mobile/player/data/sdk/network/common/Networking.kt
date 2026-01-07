package ag.sportradar.mobile.player.data.sdk.network.common

import IsolatedKoinComponent
import ag.sportradar.mobile.player.data.sdk.BuildConfig
import ag.sportradar.mobile.player.data.sdk.injection.DispatcherType
import ag.sportradar.mobile.player.data.sdk.loging.PlayerEventLogger
import ag.sportradar.mobile.player.data.sdk.network.common.errors.HttpError
import ag.sportradar.mobile.player.data.sdk.network.common.errors.NetworkErrorResolver
import ag.sportradar.mobile.player.data.sdk.network.common.errors.ResolverType
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import io.ktor.serialization.JsonConvertException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.qualifier

/**
 * Provides network request functionality using Ktor.
 * This object is responsible for making network requests and handling responses.
 */
internal object Networking : IsolatedKoinComponent {
    private val ioDispatcher: CoroutineDispatcher by inject(qualifier(DispatcherType.IO))

    /**
     * The HTTP client used for making network requests.
     */
    private val httpClient: HttpClient
        get() = get()

    /**
     * Resolver for parsing a [HttpError] out of a network response.
     */
    private val completeErrorResolver: NetworkErrorResolver
        get() = get<NetworkErrorResolver>(qualifier(ResolverType.COMPLETE))

    /**
     * Makes a GET request to the specified [url] and returns a [NetworkResponse] with the deserialized result.
     *
     * @param url The URL to make the request to.
     * @param additionalHeaders A lambda for adding additional headers to the request.
     * @return A [NetworkResponse] containing the result of the request.
     */
    suspend inline fun <reified R> get(
        url: Url,
        crossinline additionalHeaders: HttpMessageBuilder.() -> Unit = {},
    ): NetworkResponse<R> =
        withContext(ioDispatcher) {
            PlayerEventLogger.logNetworkRequest(url.toString(), "GET")
            netCall {
                httpClient.get {
                    configureRoute(url, additionalHeaders)
                }
            }
        }

    /**
     * Makes a POST request to the specified [url] with the given [body] and returns a [NetworkResponse] with the deserialized result.
     *
     * @param url The URL to make the request to.
     * @param body The request body to send. Use [EmptyContent] for no body.
     * @param additionalHeaders A lambda for adding additional headers to the request.
     * @return A [NetworkResponse] containing the result of the request.
     */
    suspend inline fun <reified R, reified B> post(
        url: Url,
        body: B,
        crossinline additionalHeaders: HttpMessageBuilder.() -> Unit = {},
    ): NetworkResponse<R> =
        withContext(ioDispatcher) {
            PlayerEventLogger.logNetworkRequest(url.toString(), "POST")
            netCall {
                httpClient.post {
                    this.contentType(ContentType.Application.Json)
                    configureRoute(url, additionalHeaders)
                    this.setBody(body)
                }
            }
        }

    /**
     * Makes a POST request to the specified [url] and returns a [NetworkResponse] with the deserialized result.
     *
     * @param url The URL to make the request to.
     * @param additionalHeaders A lambda for adding additional headers to the request.
     * @return A [NetworkResponse] containing the result of the request.
     */
    suspend inline fun <reified R> post(
        url: Url,
        crossinline additionalHeaders: HttpMessageBuilder.() -> Unit = {},
    ): NetworkResponse<R> =
        withContext(ioDispatcher) {
            PlayerEventLogger.logNetworkRequest(url.toString(), "POST")
            netCall {
                httpClient.post {
                    this.contentType(ContentType.Application.Json)
                    configureRoute(url, additionalHeaders)
                }
            }
        }

    /**
     * Makes a PUT request to the specified [url] with the given [body] and returns a [NetworkResponse] with the deserialized result.
     *
     * @param url The URL to make the request to.
     * @param body The request body to send. Use [EmptyContent] for no body.
     * @param additionalHeaders A lambda for adding additional headers to the request.
     * @return A [NetworkResponse] containing the result of the request.
     */
    suspend inline fun <reified R, reified B> put(
        url: Url,
        body: B,
        crossinline additionalHeaders: HttpMessageBuilder.() -> Unit = {},
    ): NetworkResponse<R> =
        withContext(ioDispatcher) {
            PlayerEventLogger.logNetworkRequest(url.toString(), "PUT")
            netCall {
                httpClient.put {
                    this.contentType(ContentType.Application.Json)
                    configureRoute(url, additionalHeaders)
                    this.setBody(body)
                }
            }
        }

    /**
     * Makes a DELETE request to the specified [url] and returns a [NetworkResponse] with the deserialized result.
     *
     * @param url The URL to make the request to.
     * @param additionalHeaders A lambda for adding additional headers to the request.
     * @return A [NetworkResponse] containing the result of the request.
     */
    suspend inline fun <reified R> delete(
        url: Url,
        crossinline additionalHeaders: HttpMessageBuilder.() -> Unit = {},
    ): NetworkResponse<R> =
        withContext(ioDispatcher) {
            PlayerEventLogger.logNetworkRequest(url.toString(), "DELETE")
            netCall {
                httpClient.delete {
                    configureRoute(url, additionalHeaders)
                }
            }
        }

    /**
     * Executes the provided network [call] and handles the response, returning a [NetworkResponse].
     *
     * @param call The network call to execute.
     * @return A [NetworkResponse] containing the result of the network call.
     */
    private suspend inline fun <reified R> netCall(call: () -> HttpResponse): NetworkResponse<R> =
        try {
            val response = call()
            if (response.status.isSuccess()) {
                // net call succeed
                try {
                    NetworkResponse.success(response.resolveBody<R>())
                } catch (t: JsonConvertException) {
                    // errors may come from successful responses - you get a HTTP 200 and what went wrong in the body,
                    // in which case deserializing to type R will probably fail and get caught here
                    PlayerEventLogger.logJsonParsingError("Network response parsing failed", t)
                    NetworkResponse.failure(completeErrorResolver.resolve(response))
                }
            } else {
                // net call actually failed
                NetworkResponse.failure(completeErrorResolver.resolve(response))
            }
        } catch (t: Throwable) {
            // any other unhandled exceptions from the net call are caught here
            PlayerEventLogger.logNetworkException("Network request completed with exception", t)
            // we don't have a response we can resolve here, return a default error
            NetworkResponse.failure(HttpError())
        }

    /**
     * Configures the [HttpRequestBuilder] with the provided [url] and [additionalHeaders].
     *
     * @param url The URL to configure the request for.
     * @param additionalHeaders A lambda for adding additional headers to the request.
     */
    private inline fun HttpRequestBuilder.configureRoute(
        url: Url,
        crossinline additionalHeaders: HttpMessageBuilder.() -> Unit,
    ) = this.url {
        protocol = url.protocol
        host = url.host
        encodedPath = url.encodedPath
        // re: using encodedParameters.append instead of parameters.append
        // encodedParameters gives you the flexibility to provide weird non-encoded chars in the url e.g. $
        // make sure to encode parameters if necessary beforehand
        encodedParameters.appendAll(url.parameters)

        header(HttpHeaders.UserAgent, BuildConfig.USER_AGENT)

        additionalHeaders()
    }
}