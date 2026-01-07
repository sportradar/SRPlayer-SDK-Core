package ag.sportradar.mobile.player.data.sdk.injection

/**
 * Represents the different types of coroutine dispatchers available for dependency injection.
 */
enum class DispatcherType {
    /** The main dispatcher, associated with the main thread. */
    MAIN,

    /** The IO dispatcher, optimized for IO-bound operations. */
    IO,

    /** The default dispatcher, used for general coroutine execution. */
    DEFAULT,

    /** The unconfined dispatcher, which does not confine coroutines to any specific thread. */
    UNCONFINED,
}