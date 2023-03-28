package co.nimblehq.loyalty.sdk.exception

sealed class LoyaltySdkException(cause: String) : RuntimeException(cause)

sealed class AuthenticationException(cause: String) : LoyaltySdkException(cause) {
    object UnableToInitSignInUrl : AuthenticationException("Could not create the sign in URL")
    object UnableToInitCallbackUrl : AuthenticationException("Could not create the callback URL")
    object UnableToAuthenticate :
        AuthenticationException("An error occurred when attempting to sign in")

    object UnauthenticatedException :
        AuthenticationException("Unauthorized access. Please authenticate.")
}

sealed class InitializationException(cause: String) : LoyaltySdkException(cause) {
    object InvalidContext : InitializationException("Context must not be null")
    object InvalidClientId : InitializationException("CLIENT_ID must not be null or empty")
    object InvalidClientSecret : InitializationException("CLIENT_SECRET must not be null or empty")
    object LateInitialization :
        InitializationException("LoyaltySdk has not been initialized. Initialize new instance by using LoyaltySdk.Builder")
}

sealed class NetworkException(cause: String) : LoyaltySdkException(cause) {
    object NoConnectivity : NetworkException("No connectivity")
    data class ApiResponse(val _cause: String) : NetworkException(_cause)
}
