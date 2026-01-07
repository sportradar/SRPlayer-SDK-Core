package ag.sportradar.mobile.player.data.sdk.utils

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel

/**
 * A Napier [Antilog] implementation that suppresses all logs.
 *
 * Use this class when you want to disable logging completely, for example in
 * production builds or when debug logs are not needed.
 */
class NoAntilog : Antilog() {

    /**
     * Overrides the default log behavior and ignores all log requests.
     *
     * @param priority The log level (e.g., DEBUG, INFO, ERROR) – ignored.
     * @param tag Optional tag identifying the source of the log – ignored.
     * @param throwable Optional throwable associated with the log – ignored.
     * @param message Optional log message – ignored.
     */
    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        // No operation: logs are ignored
    }
}
