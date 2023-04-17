package co.nimblehq.loyalty.sdk.repository

import co.nimblehq.loyalty.sdk.api.ApiService
import co.nimblehq.loyalty.sdk.api.response.toModels
import co.nimblehq.loyalty.sdk.model.Product

internal interface ProductRepository {
    suspend fun getProductList(): List<Product>
}

internal class ProductRepositoryImpl(
    private val apiService: ApiService
) : ProductRepository {

    override suspend fun getProductList(): List<Product> {
        return apiService.getProducts().toModels()
    }
}
