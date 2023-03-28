package co.nimblehq.loyalty.sdk.repository

import co.nimblehq.common.extensions.isNotNullOrEmpty
import co.nimblehq.loyalty.sdk.api.AuthenticationService
import co.nimblehq.loyalty.sdk.api.response.toModel
import co.nimblehq.loyalty.sdk.model.AuthenticationState
import co.nimblehq.loyalty.sdk.model.OauthToken
import co.nimblehq.loyalty.sdk.persistence.AuthPersistence

internal interface AuthenticationRepository {
    suspend fun requestAccessToken(
        clientId: String,
        clientSecret: String,
        code: String,
        redirectUri: String,
    ): OauthToken

    suspend fun getAuthenticationState(): AuthenticationState

    suspend fun clearAuthenticateState()
}

internal class AuthenticationRepositoryImpl(
    private val authenticationService: AuthenticationService,
    private val authPersistence: AuthPersistence
) : AuthenticationRepository {

    override suspend fun requestAccessToken(
        clientId: String,
        clientSecret: String,
        code: String,
        redirectUri: String,
    ): OauthToken {
        return authenticationService.requestAccessToken(
            clientId = clientId,
            clientSecret = clientSecret,
            code = code,
            redirectUri = redirectUri
        ).toModel()
    }

    override suspend fun getAuthenticationState(): AuthenticationState {
        val accessToken = authPersistence.getAccessToken().isNotNullOrEmpty()
        val tokenType = authPersistence.getAccessToken().isNotNullOrEmpty()
        return when {
            accessToken && tokenType -> AuthenticationState.Authenticated
            else -> AuthenticationState.Anonymous
        }
    }

    override suspend fun clearAuthenticateState() {
        authPersistence.clearAll()
    }
}