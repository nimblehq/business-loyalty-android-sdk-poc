package co.nimblehq.loyalty.sdk.model

sealed class AuthenticationState {
    object Authenticated : AuthenticationState()
    object Anonymous : AuthenticationState()
}
