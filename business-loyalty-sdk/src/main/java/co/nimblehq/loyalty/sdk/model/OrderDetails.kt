package co.nimblehq.loyalty.sdk.model

data class OrderDetails(
    val id: String? = null,
    val organizationId: String? = null,
    val userId: String? = null,
    val status: String? = null,
    val currency: String? = null,
    val subtotalPrice: String? = null,
    val finalPrice: String? = null,
    val shipmentCost: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val orderLineItems: List<OrderLineItem>? = null
) {
    data class OrderLineItem(
        val id: String? = null,
        val organizationId: String? = null,
        val userId: String? = null,
        val productId: String? = null,
        val price: String? = null,
        val discount: String? = null,
        val tax: String? = null,
        val finalPrice: String? = null,
        val netPrice: String? = null,
        val quantity: Int? = null,
        val status: String? = null,
        val createdAt: String? = null,
        val updatedAt: String? = null,
        val orderId: String? = null
    )
}
