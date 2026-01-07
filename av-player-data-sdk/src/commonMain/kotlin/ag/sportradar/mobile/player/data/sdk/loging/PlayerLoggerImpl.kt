package ag.sportradar.mobile.player.data.sdk.loging

import io.github.aakira.napier.Napier


/**
 * Implementation of [PlayerLoggingProvider] responsible for handling logging operations within the SDK.
 *
 * This class acts as the central logging mechanism, managing how log messages are filtered and
 * dispatched based on the provided [LoggerConfiguration]. It supports standard logging levels
 * (ERROR, WARNING, INFO, DEBUG) and utilizes [Napier] for the final console output.
 *
 * Key responsibilities:
 * - **Filtering:** Checks if a log message's priority meets the minimum required level defined in [loggerConfiguration].
 *   If no configuration is provided, it defaults to [LogLevel.VERBOSE].
 * - **Dispatching:** Routes valid log messages to the underlying logging utility (Napier).
 *
 * @property loggerConfiguration The optional configuration defining the minimum logging level.
 */
class PlayerLoggerImpl(val loggerConfiguration: LoggerConfiguration?) : PlayerLoggingProvider {

    override fun error(message: String, tag: String, throwable: Throwable?) {
        log(LogLevel.ERROR, message, tag, throwable)
    }

    override fun warning(message: String, tag: String, throwable: Throwable?) {
        log(LogLevel.WARNING, message, tag, throwable)
    }

    override fun info(message: String, tag: String, throwable: Throwable?) {
        log(LogLevel.INFO, message, tag, throwable)
    }

    override fun debug(message: String, tag: String, throwable: Throwable?) {
        log(LogLevel.DEBUG, message, tag, throwable)
    }

    override fun verbose(message: String, tag: String, throwable: Throwable?) {
        log(LogLevel.VERBOSE, message, tag, throwable)
    }

    private fun log(level: LogLevel, message: String, tag: String, throwable: Throwable?) {
        if (level.priority < (loggerConfiguration?.minLogLevel?.priority ?: LogLevel.VERBOSE.priority)) {
            return
        }

        logToConsole(level, tag, message, throwable)
    }

    private fun logToConsole(level: LogLevel, tag: String, message: String, throwable: Throwable?) {
        when (level) {
            LogLevel.ERROR -> Napier.e(message, throwable, tag)
            LogLevel.WARNING -> Napier.w(message, throwable, tag)
            LogLevel.INFO -> Napier.i(message, throwable, tag)
            LogLevel.DEBUG -> Napier.d(message, throwable, tag)
            LogLevel.VERBOSE -> Napier.v(message, throwable, tag)
            LogLevel.NONE -> {}
        }
    }
}