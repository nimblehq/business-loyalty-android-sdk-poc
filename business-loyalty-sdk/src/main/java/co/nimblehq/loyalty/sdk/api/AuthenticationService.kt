package co.nimblehq.loyalty.sdk.api

import co.nimblehq.loyalty.sdk.api.response.OauthTokenResponse
import retrofit2.http.POST
import retrofit2.http.Query

private const val AUTHORIZATION_CODE = "authorization_code"

internal interface AuthenticationService {

    @POST("token")
    suspend fun requestAccessToken(
        @Query("grant_type") grantType: String = AUTHORIZATION_CODE,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("code") code: String,
        @Query("redirect_uri") redirectUri: String
    ): OauthTokenResponse
}
