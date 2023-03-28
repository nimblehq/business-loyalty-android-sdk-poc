package co.nimblehq.loyalty.sdk.api.response

import co.nimblehq.loyalty.sdk.model.OauthToken
import com.squareup.moshi.Json

data class OauthTokenResponse(
    @Json(name = "access_token")
    val accessToken: String? = null,
    @Json(name = "token_type")
    val tokenType: String? = null,
    @Json(name = "expires_in")
    val expiresIn: Long? = null,
    @Json(name = "scope")
    val scope: String? = null,
    @Json(name = "created_at")
    val createdAt: Long? = null
)

fun OauthTokenResponse.toModel() = OauthToken(
    accessToken = accessToken,
    tokenType = tokenType,
    expiresIn = expiresIn,
    scope = scope,
    createdAt = createdAt
)
