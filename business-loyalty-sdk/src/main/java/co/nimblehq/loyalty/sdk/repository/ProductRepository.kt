package co.nimblehq.loyalty.sdk.repository

import co.nimblehq.loyalty.sdk.api.ApiService
import co.nimblehq.loyalty.sdk.api.response.toModel
import co.nimblehq.loyalty.sdk.api.response.toModels
import co.nimblehq.loyalty.sdk.model.Product

internal interface ProductRepository {
    suspend fun getProductList(): List<Product>
    suspend fun getProductDetail(productId: String): Product
}

internal class ProductRepositoryImpl(
    private val apiService: ApiService
) : ProductRepository {

    override suspend fun getProductList(): List<Product> {
        return apiService.getProducts().toModels()
    }

    override suspend fun getProductDetail(productId: String): Product {
        return apiService.getProductDetail(productId).toModel()
    }
}
