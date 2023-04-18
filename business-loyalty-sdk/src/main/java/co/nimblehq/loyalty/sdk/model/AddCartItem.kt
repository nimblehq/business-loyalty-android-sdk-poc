package co.nimblehq.loyalty.sdk.model

import co.nimblehq.loyalty.sdk.api.request.AddCartItemRequest

data class AddCartItem(
    val productId: String,
    val quantity: Int,
)

fun AddCartItem.toRequest() = AddCartItemRequest(
    productId,
    quantity
)
