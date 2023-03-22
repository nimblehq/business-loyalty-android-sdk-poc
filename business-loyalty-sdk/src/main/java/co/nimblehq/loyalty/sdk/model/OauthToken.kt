package co.nimblehq.loyalty.sdk.model

data class OauthToken(
    val accessToken: String? = null,
    val tokenType: String? = null,
    val expiresIn: Long? = null,
    val scope: String? = null,
    val createdAt: Long? = null
)
