package co.nimblehq.loyalty.sdk.api.response

import co.nimblehq.loyalty.sdk.model.Product
import com.squareup.moshi.Json

data class ProductResponse(
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "organization_id")
    val organizationId: String? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "sku")
    val sku: String? = null,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "price")
    val price: String? = null,
    @Json(name = "display_price")
    val displayPrice: String? = null,
    @Json(name = "created_at")
    val createdAt: String? = null,
    @Json(name = "updated_at")
    val updatedAt: String? = null,
    @Json(name = "image_url")
    val imageUrl: String? = null
)

fun ProductResponse.toModel() = Product(
    id = id.orEmpty(),
    organizationId = organizationId.orEmpty(),
    name = name.orEmpty(),
    sku = sku.orEmpty(),
    description = description.orEmpty(),
    price = price.orEmpty(),
    displayPrice = displayPrice.orEmpty(),
    createdAt = createdAt.orEmpty(),
    updatedAt = updatedAt.orEmpty(),
    imageUrl = imageUrl.orEmpty()
)

fun List<ProductResponse>.toModels() = this.map { product -> product.toModel() }
