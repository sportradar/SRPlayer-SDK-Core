package ag.sportradar.mobile.player.data.sdk.network.common.errors

/**
 * Represents the types of error resolvers used in the player network layer.
 *
 * - `DEFAULT`: Standard resolver type.
 * - `FISHNET`: Resolver for Fishnet-specific errors.
 * - `RESPONSE_STATUS`: Resolver based on response status.
 * - `COMPLETE`: Resolver for complete error handling.
 */
internal enum class ResolverType {
    DEFAULT,
    FISHNET,
    RESPONSE_STATUS,
    COMPLETE,
}
