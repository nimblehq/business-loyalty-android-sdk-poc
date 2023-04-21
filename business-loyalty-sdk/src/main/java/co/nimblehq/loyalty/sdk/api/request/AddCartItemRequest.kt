package co.nimblehq.loyalty.sdk.api.request

import com.squareup.moshi.Json

data class AddCartItemRequest(
    @Json(name = "product_id")
    val productId: String,
    @Json(name = "quantity")
    val quantity: Int
)
