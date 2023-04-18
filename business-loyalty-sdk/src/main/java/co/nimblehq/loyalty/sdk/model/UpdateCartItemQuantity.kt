package co.nimblehq.loyalty.sdk.model

import co.nimblehq.loyalty.sdk.api.request.UpdateCartItemQuantityRequest

data class UpdateCartItemQuantity(
    val itemId: String,
    val quantity: Int,
)

fun UpdateCartItemQuantity.toRequest() = UpdateCartItemQuantityRequest(
    quantity
)
