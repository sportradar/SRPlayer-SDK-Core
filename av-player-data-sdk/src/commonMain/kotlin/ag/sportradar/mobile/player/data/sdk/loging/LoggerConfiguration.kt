package ag.sportradar.mobile.player.data.sdk.loging


/**
 * Configuration settings for the SDK logger.
 *
 * This class allows customization of the logging behavior, such as enabling/disabling logging
 * and setting the minimum severity level for logs to be recorded.
 *
 * @property minLogLevel The minimum [LogLevel] required for a message to be logged. Messages with a priority lower than this will be ignored. Defaults to [LogLevel.ERROR].
 */
data class LoggerConfiguration(
    val minLogLevel: LogLevel = LogLevel.ERROR,
)