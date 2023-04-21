package co.nimblehq.loyalty.sdk.repository

import co.nimblehq.loyalty.sdk.api.ApiService
import co.nimblehq.loyalty.sdk.api.response.toModel
import co.nimblehq.loyalty.sdk.model.Cart

internal interface CartRepository {
    suspend fun getCart(): Cart
}

internal class CartRepositoryImpl(
    private val apiService: ApiService
) : CartRepository {

    override suspend fun getCart(): Cart {
        return apiService.getCart().toModel()
    }
}
