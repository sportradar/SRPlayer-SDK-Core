package ag.sportradar.mobile.player.data.sdk.loging


/**
 * Defines the different levels of logging available in the SDK.
 *
 * Each level has an associated integer [priority]. A higher priority value indicates a more
 * severe log level. The SDK's logger will only output logs that have a priority equal to or
 * greater than the configured minimum log level.
 *
 * @property priority The integer priority of the log level. Higher numbers are more critical.
 */
enum class LogLevel(val priority: Int) {
    VERBOSE(0),
    INFO(1),
    DEBUG(2),
    WARNING(3),
    ERROR(4),
    NONE(10)
}