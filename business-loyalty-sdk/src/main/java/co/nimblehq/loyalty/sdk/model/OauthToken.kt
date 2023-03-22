package co.nimblehq.loyalty.sdk.model

data class OauthToken(
    val accessToken: String?,
    val tokenType: String?,
    val expiresIn: Long?,
    val scope: String?,
    val createdAt: Long?,
)
