package co.nimblehq.loyalty.sdk.api.request

import com.squareup.moshi.Json

abstract class BaseRequest {
    @Json(name = "client_id")
    var clientId: String = credentials.clientId

    @Json(name = "client_secret")
    var clientSecret: String = credentials.clientSecret

    companion object {
        private var credentials = Credentials()

        fun updateAuthenticationInfo(credentialsInfo: Credentials) {
            credentials = credentialsInfo
        }
    }
}
