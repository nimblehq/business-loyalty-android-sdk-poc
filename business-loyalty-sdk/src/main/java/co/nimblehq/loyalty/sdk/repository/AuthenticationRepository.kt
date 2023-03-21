package co.nimblehq.loyalty.sdk.repository

import co.nimblehq.loyalty.sdk.api.AuthenticationService
import co.nimblehq.loyalty.sdk.api.response.toModel
import co.nimblehq.loyalty.sdk.model.OauthToken

internal interface AuthenticationRepository {
    suspend fun requestAccessToken(
        clientId: String,
        clientSecret: String,
        code: String,
        redirectUri: String,
    ): OauthToken
}

internal class AuthenticationRepositoryImpl(
    private val authenticationService: AuthenticationService
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
}