package co.nimblehq.loyalty.sdk.model

data class Order(
    val id: String? = null,
    val organizationId: String? = null,
    val userId: String? = null,
    val status: String? = null,
    val currency: String? = null,
    val subtotalPrice: String? = null,
    val finalPrice: String? = null,
    val shipmentCost: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
