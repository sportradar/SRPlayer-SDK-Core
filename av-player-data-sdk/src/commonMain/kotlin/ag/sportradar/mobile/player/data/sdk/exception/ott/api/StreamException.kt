package ag.sportradar.mobile.player.data.sdk.exception.ott.api

import ag.sportradar.mobile.player.data.sdk.exception.SRAVPlayerException

interface StreamException : SRAVPlayerException
interface FlashStreamError : StreamException

/**
 * Indicates that additional livestream feeds could not be loaded.
 * This error typically occurs when the multiview interface fails to initialize or retrieve feeds.
 */
object LiveStreamsLoadError : StreamException {
    override val internalCode: Int = 400
    override val externalCode: Int = 400
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "Unable to load additional livestream feeds."
    override val errorMessage: String = "Multiview interface could not be loaded."
}

/**
 * Indicates that no valid video source was found for playback.
 * This error typically occurs when the video src or mimeType format is missing or invalid.
 */
object VideoFormatError : StreamException {
    override val internalCode: Int = 600
    override val externalCode: Int = 600
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "No valid video source found for playback."
    override val errorMessage: String = "No valid video src or mimeType format detected."
}

/**
 * Indicates that no valid video URL was provided for playback.
 * This error typically occurs when the interface fails to send a valid videoURL.
 */
object VideoUrlError : StreamException {
    override val internalCode: Int = 601
    override val externalCode: Int = 601
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "No valid video URL provided."
    override val errorMessage: String = "Interface did not send a valid videoURL."
}

object VideoPlaybackError : StreamException {
    override val internalCode: Int = 602
    override val externalCode: Int = 602
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "No playable stream sources available for playback."
    override val errorMessage: String = "All available stream sources failed to play."
}

object VideoNetworkError : StreamException {
    override val internalCode: Int = 610
    override val externalCode: Int = 610
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "Non-fatal network error occurred during playback."
    override val errorMessage: String = "hls.js non-fatal error (Youbora event only)."
}

object VideoStreamPlaybackError : StreamException {
    override val internalCode: Int = 700
    override val externalCode: Int = 700
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "Playback error occurred in video stream."
    override val errorMessage: String = "Video stream playback failed."
}

object DrmDashLicenseServerError : StreamException {
    override val internalCode: Int = 701
    override val externalCode: Int = 701
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "DRM DASH license server is not available."
    override val errorMessage: String = "Failed to communicate with DASH license server."
}

object DrmDashDecryptionError : StreamException {
    override val internalCode: Int = 702
    override val externalCode: Int = 702
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "DRM DASH stream cannot be decrypted."
    override val errorMessage: String = "Decryption of DASH stream failed."
}

object DrmFairplayCertificateError : StreamException {
    override val internalCode: Int = 703
    override val externalCode: Int = 703
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "Failed to retrieve the Fairplay server certificate."
    override val errorMessage: String = "DRM Fairplay certificate retrieval error."
}

object StreamEndedInfo : StreamException {
    override val internalCode: Int = 800
    override val externalCode: Int = 800
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "This stream has ended."
    override val errorMessage: String = "Stream ended."
}

object BrowserNotSupportedInfo : StreamException {
    override val internalCode: Int = 801
    override val externalCode: Int = 801
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "Your browser is not supported for playback."
    override val errorMessage: String = "Browser not supported."
}

object FlashSecurityError : StreamException {
    override val internalCode: Int = 900
    override val externalCode: Int = 900
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "crossdomain.xml file not found on the server domain."
    override val errorMessage: String = "Flash security error."
}

object FlashHttpError : FlashStreamError {
    override val internalCode: Int = 901
    override val externalCode: Int = 901
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "HTTP error occurred during Flash stream playback."
    override val errorMessage: String = "Flash HTTP error."
}

object FlashMediaElementError : FlashStreamError {
    override val internalCode: Int = 902
    override val externalCode: Int = 902
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "Cannot create media element with the provided URL and mimetype."
    override val errorMessage: String = "Flash media element creation error."
}

object FlashSmilFileError : FlashStreamError {
    override val internalCode: Int = 903
    override val externalCode: Int = 903
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "SMIL file is empty or not in the correct format."
    override val errorMessage: String = "SMIL file parsing error."
}

object FlashSwfFileMissing : FlashStreamError {
    override val internalCode: Int = 904
    override val externalCode: Int = 904
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "SWF file is missing. Player not loaded."
    override val errorMessage: String = "SWF file missing error."
}

object FlashPluginNotActive : FlashStreamError {
    override val internalCode: Int = 905
    override val externalCode: Int = 905
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "Flash plugin is not active. Please activate Flash in your browser."
    override val errorMessage: String = "Flash plugin not active error."
}

object FlashSeekError : FlashStreamError {
    override val internalCode: Int = 906
    override val externalCode: Int = 906
    override val errorTitle: String = "Stream Error"
    override val userMessage: String = "Seek operation is out of bounds."
    override val errorMessage: String = "Flash seek error."
}
