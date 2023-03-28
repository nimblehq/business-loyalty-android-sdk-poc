package co.nimblehq.loyalty.sdk.api.response

import com.squareup.moshi.Json

data class ErrorResponse(
    @Json(name = "error")
    val error: String
)
