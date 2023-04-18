package co.nimblehq.loyalty.sdk.model

data class Product(
    val id: String?,
    val organizationId: String?,
    val name: String?,
    val sku: String?,
    val description: String?,
    val price: String?,
    val displayPrice: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val imageUrl: String?
)
