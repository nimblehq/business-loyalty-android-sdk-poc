package co.nimblehq.loyalty.sdk.api.response

import co.nimblehq.loyalty.sdk.model.OrderDetails
import com.squareup.moshi.Json

data class OrderDetailsResponse(
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
    val updatedAt: String? = null,
    @Json(name = "order_line_items")
    val orderLineItems: List<OrderLineItemResponse>? = null,
) {
    data class OrderLineItemResponse(
        @Json(name = "id")
        val id: String? = null,
        @Json(name = "organization_id")
        val organizationId: String? = null,
        @Json(name = "user_id")
        val userId: String? = null,
        @Json(name = "product_id")
        val productId: String? = null,
        @Json(name = "price")
        val price: String? = null,
        @Json(name = "discount")
        val discount: String? = null,
        @Json(name = "tax")
        val tax: String? = null,
        @Json(name = "final_price")
        val finalPrice: String? = null,
        @Json(name = "net_price")
        val netPrice: String? = null,
        @Json(name = "quantity")
        val quantity: Int? = null,
        @Json(name = "status")
        val status: String? = null,
        @Json(name = "created_at")
        val createdAt: String? = null,
        @Json(name = "updated_at")
        val updatedAt: String? = null,
        @Json(name = "order_id")
        val orderId: String? = null,
        @Json(name = "product")
        val product: ProductResponse? = null,
    )
}

fun OrderDetailsResponse.toModel() = OrderDetails(
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
    orderLineItems = orderLineItems?.toModels(),
)

fun OrderDetailsResponse.OrderLineItemResponse.toModel() = OrderDetails.OrderLineItem(
    id = id,
    organizationId = organizationId,
    userId = userId,
    productId = productId,
    price = price,
    discount = discount,
    tax = tax,
    finalPrice = finalPrice,
    netPrice = netPrice,
    quantity = quantity,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
    orderId = orderId,
    product = product?.toModel(),
)

fun List<OrderDetailsResponse.OrderLineItemResponse>.toModels() =
    this.map { orderLineItem -> orderLineItem.toModel() }
