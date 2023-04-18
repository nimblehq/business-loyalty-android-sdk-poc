package co.nimblehq.loyalty.sdk.api.request

import com.squareup.moshi.Json

data class UpdateCartItemQuantityRequest(
    @Json(name = "quantity")
    val quantity: Int
)
