package ag.sportradar.mobile.player.data.sdk.exception.ott.api

import ag.sportradar.mobile.player.data.sdk.exception.SRAVPlayerException

interface ConfigException : SRAVPlayerException

/**
 * Indicates that the configuration file could not be loaded.
 * Typically occurs when the file is missing or inaccessible.
 */
object ConfigNotLoaded : ConfigException {
    override val internalCode: Int = 100
    override val externalCode: Int = 100
    override val errorTitle: String = "Config Error"
    override val userMessage: String = "The configuration file could not be loaded."
    override val errorMessage: String = "Configuration file loading failed."
}

/**
 * Represents an error that occurred while loading the configuration file from an external source.
 */
object ConfigExternalError : ConfigException {
    override val internalCode: Int = 101
    override val externalCode: Int = 101
    override val errorTitle: String = "Config Error"
    override val userMessage: String = "An error occurred while loading the configuration file."
    override val errorMessage: String = "External configuration error."
}

/**
 * Indicates a protocol-related security error during configuration loading.
 */
object ConfigSecurityProtocolError : ConfigException {
    override val internalCode: Int = 102
    override val externalCode: Int = 102
    override val errorTitle: String = "Config Error"
    override val userMessage: String = "A protocol error occurred while loading the configuration."
    override val errorMessage: String = "Security protocol error in configuration."
}

/**
 * Indicates a CORS-related security error during configuration loading.
 */
object ConfigSecurityCORSError : ConfigException {
    override val internalCode: Int = 103
    override val externalCode: Int = 103
    override val errorTitle: String = "Config Error"
    override val userMessage: String = "A CORS error occurred while loading the configuration."
    override val errorMessage: String = "Security CORS error in configuration."
}

/**
 * Indicates that an invalid date format was provided in the configuration, typically when countdown is enabled.
 */
object ConfigInvalidDateFormat : ConfigException {
    override val internalCode: Int = 104
    override val externalCode: Int = 104
    override val errorTitle: String = "Config Error"
    override val userMessage: String = "Invalid date format provided in configuration."
    override val errorMessage: String = "Countdown enabled but invalid date format passed via configuration."
}
