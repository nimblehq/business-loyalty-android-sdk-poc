package co.nimblehq.loyalty.sdk.api.response

import co.nimblehq.loyalty.sdk.model.Order
import com.squareup.moshi.Json

data class OrderResponse(
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "organization_id")
    val organizationId: String? = null,
    @Json(name = "user_id")
    val userId: String? = null,
    @Json(name = "status")
    val status: String? = null,
    @Json(name = "currency")
    val currency: String? = null,
    @Json(name = "subtotal_price")
    val subtotalPrice: String? = null,
    @Json(name = "final_price")
    val finalPrice: String? = null,
    @Json(name = "shipment_cost")
    val shipmentCost: String? = null,
    @Json(name = "created_at")
    val createdAt: String? = null,
    @Json(name = "updated_at")
    val updatedAt: String? = null
)

fun OrderResponse.toModel() = Order(
    id = id,
    organizationId = organizationId,
    userId = userId,
    status = status,
    currency = currency,
    subtotalPrice = subtotalPrice,
    finalPrice = finalPrice,
    shipmentCost = shipmentCost,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun List<OrderResponse>.toModels() = this.map { order -> order.toModel() }
