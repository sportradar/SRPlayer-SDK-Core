package ag.sportradar.mobile.player.data.sdk.exception

import ag.sportradar.mobile.player.data.sdk.exception.ott.api.BrowserNotSupportedInfo
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.ConfigExternalError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.ConfigInvalidDateFormat
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.ConfigNotLoaded
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.ConfigSecurityCORSError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.ConfigSecurityProtocolError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.DrmDashDecryptionError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.DrmDashLicenseServerError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.DrmFairplayCertificateError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.EmbedDomObjectNotAvailable
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.EmbedVideoOrPartnerIdMissing
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.FlashHttpError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.FlashMediaElementError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.FlashPluginNotActive
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.FlashSecurityError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.FlashSeekError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.FlashSmilFileError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.FlashSwfFileMissing
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.LiveStreamsLoadError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamAccessApiNotLoaded
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamAccessApplicationError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamAccessBadRequest
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamAccessError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamAccessGeoBlock
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamAccessMethodNotAllowed
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamAccessResourceNotFound
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamAccessStreamNotAvailable
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamAccessStreamNotFound
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamDataGenericError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamDataGeoBlock
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamDataNotLoaded
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamDataStreamNotAvailable
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamDataStreamRemoved
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamDataTokenExpired
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamDataTokenNotValid
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamEndedInfo
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamNotAllowedOnMobile
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.StreamNotAllowedOnMultipleDevices
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.VideoFormatError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.VideoNetworkError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.VideoPlaybackError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.VideoStreamPlaybackError
import ag.sportradar.mobile.player.data.sdk.exception.ott.api.VideoUrlError

/**
 * Represents a generic exception for the AV Player SDK.
 * Implementations provide details about specific error scenarios,
 * including internal and external codes, error titles, and user-facing messages.
 */
interface SRAVPlayerException {
    /** Internal error code used for SDK diagnostics. */
    val internalCode: Int
    /** External error code exposed to consumers of the SDK. */
    val externalCode: Int
    /** The error title displayed when this exception occurs. */
    val errorTitle: String
    /** The message shown to end users describing the error. */
    val userMessage: String
    /** Detailed error message for logging or debugging purposes. */
    val errorMessage: String
}

/**
 * Represents a custom AV Player exception with user-defined codes and messages.
 * Useful for dynamic or application-specific error scenarios.
 */
data class CustomException(
    override val internalCode: Int,
    override val externalCode: Int,
    override val errorTitle: String,
    override val userMessage: String,
    override val errorMessage: String
) : SRAVPlayerException

/**
 * Represents an unknown error in the AV Player SDK.
 * Used when the error cause cannot be determined.
 */
object UnknownException : SRAVPlayerException {
    override val internalCode: Int = 990
    override val externalCode: Int = 990
    override val errorTitle: String = "Unknown Error"
    override val userMessage: String = "An unknown error has occurred."
    override val errorMessage: String = "An unknown error has occurred in the AV Player SDK."
}

/**
 * Represents a network error in the AV Player SDK.
 * Indicates issues with connectivity or network services.
 */
object NetworkException : SRAVPlayerException {
    override val internalCode: Int = 991
    override val externalCode: Int = 991
    override val errorTitle: String = "Network Error"
    override val userMessage: String = "A network error has occurred. Please check your connection."
    override val errorMessage: String =
        "A network error occurred while trying to access the AV Player SDK services."
}

/**
 * Represents a licence authentication failure in the AV Player SDK.
 * Thrown when no valid licence is found or authentication fails.
 */
object LicenceException : SRAVPlayerException {
    override val internalCode: Int = 992
    override val externalCode: Int = 992
    override val errorTitle: String = "Licence Error"
    override val userMessage: String = "Licence authentication failed. No valid licence was found."
    override val errorMessage: String = "The AV Player SDK could not authenticate due to a missing or invalid licence."
}

/**
 * Represents an asset error in the AV Player SDK.
 * Indicates issues with loading or accessing media assets.
 */
object AssetException : SRAVPlayerException {
    override val internalCode: Int = 993
    override val externalCode: Int = 993
    override val errorTitle: String = "Asset Error"
    override val userMessage: String = "A problem occurred while loading the media asset."
    override val errorMessage: String =
        "An error occurred while trying to load or access a media asset in the AV Player SDK."
}

/**
 * Finds a stream access exception by its external code.
 * @param code The external error code.
 * @return The matching exception or null if not found.
 */
fun getExceptionByExternalCode(code: Int): SRAVPlayerException? {
    return createListOfSRAVPlayerException().find { it.externalCode == code }
}

/**
 * Finds a stream access exception by its internal code.
 * @param code The internal error code.
 * @return The matching exception or null if not found.
 */
fun getExceptionByInternalCode(code: Int): SRAVPlayerException? {
    return createListOfSRAVPlayerException().find { it.internalCode == code }
}

/**
 * Creates a list of all available AV Player exceptions.
 * Used for searching and mapping error codes to exception objects.
 */
private fun createListOfSRAVPlayerException(): List<SRAVPlayerException> =
    mutableListOf<SRAVPlayerException>().apply {
        add(UnknownException)
        add(NetworkException)
        add(LicenceException)
        addAll(createListOfHeartbeatException())
        addAll(createListOfChromeCastException())
        addAll(createListOfConfigException())
        addAll(createListOfEmbedException())
        addAll(createListOfStreamDataException())
        addAll(createListOfStreamAccessException())
        addAll(createListOfStreamException())
    }

/** Creates a list of heartbeat-related exceptions. */
private fun createListOfHeartbeatException(): List<SRAVPlayerException> =
    mutableListOf<SRAVPlayerException>().apply {
        add(HeartbeatKeyLoadError)
        add(HeartbeatKeyError)
    }

/** Creates a list of Chromecast-related exceptions. */
private fun createListOfChromeCastException(): List<SRAVPlayerException> =
    mutableListOf<SRAVPlayerException>().apply { add(ChromecastError) }

/** Creates a list of configuration-related exceptions. */
private fun createListOfConfigException(): List<SRAVPlayerException> =
    mutableListOf<SRAVPlayerException>().apply {
        add(ConfigNotLoaded)
        add(ConfigExternalError)
        add(ConfigSecurityProtocolError)
        add(ConfigSecurityCORSError)
        add(ConfigInvalidDateFormat)
    }

/** Creates a list of embed-related exceptions. */
private fun createListOfEmbedException(): List<SRAVPlayerException> =
    mutableListOf<SRAVPlayerException>().apply {
        add(EmbedVideoOrPartnerIdMissing)
        add(EmbedDomObjectNotAvailable)
    }

/** Creates a list of stream access-related exceptions. */
private fun createListOfStreamAccessException(): List<SRAVPlayerException> =
    mutableListOf<SRAVPlayerException>().apply {
        add(StreamAccessApiNotLoaded)
        add(StreamNotAllowedOnMultipleDevices)
        add(StreamNotAllowedOnMobile)
        add(StreamAccessError)
        add(StreamAccessResourceNotFound)
        add(StreamAccessMethodNotAllowed)
        add(StreamAccessApplicationError)
        add(StreamAccessBadRequest)
        add(StreamAccessStreamNotAvailable)
        add(StreamAccessGeoBlock)
        add(StreamAccessStreamNotFound)
    }

/** Creates a list of stream data-related exceptions. */
private fun createListOfStreamDataException(): List<SRAVPlayerException> =
    mutableListOf<SRAVPlayerException>().apply {
        add(StreamDataNotLoaded)
        add(StreamDataGeoBlock)
        add(StreamDataTokenNotValid)
        add(StreamDataStreamNotAvailable)
        add(StreamDataTokenExpired)
        add(StreamDataStreamRemoved)
        add(StreamDataGenericError)
    }

/** Creates a list of general stream-related exceptions. */
private fun createListOfStreamException(): List<SRAVPlayerException> =
    mutableListOf<SRAVPlayerException>().apply {
        add(LiveStreamsLoadError)
        add(VideoFormatError)
        add(VideoUrlError)
        add(VideoPlaybackError)
        add(VideoNetworkError)
        add(VideoStreamPlaybackError)
        add(DrmDashLicenseServerError)
        add(DrmDashDecryptionError)
        add(DrmFairplayCertificateError)
        add(StreamEndedInfo)
        add(BrowserNotSupportedInfo)
        add(FlashSecurityError)
        add(FlashHttpError)
        add(FlashMediaElementError)
        add(FlashSmilFileError)
        add(FlashSwfFileMissing)
        add(FlashPluginNotActive)
        add(FlashSeekError)
    }

