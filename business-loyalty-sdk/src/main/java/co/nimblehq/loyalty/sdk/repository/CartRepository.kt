package co.nimblehq.loyalty.sdk.repository

import co.nimblehq.loyalty.sdk.api.ApiService
import co.nimblehq.loyalty.sdk.api.response.toModel
import co.nimblehq.loyalty.sdk.model.AddCartItem
import co.nimblehq.loyalty.sdk.model.Cart
import co.nimblehq.loyalty.sdk.model.UpdateCartItemQuantity
import co.nimblehq.loyalty.sdk.model.toRequest

internal interface CartRepository {
    suspend fun getCart(): Cart
    suspend fun addCartItem(addCartItem: AddCartItem): Cart
    suspend fun updateCartItemQuantity(updateCartItemQuantity: UpdateCartItemQuantity): Cart
    suspend fun removeCartItem(itemId: String): Cart
}

internal class CartRepositoryImpl(
    private val apiService: ApiService
) : CartRepository {

    override suspend fun getCart(): Cart {
        return apiService.getCart().toModel()
    }

    override suspend fun addCartItem(addCartItem: AddCartItem): Cart {
        val request = addCartItem.toRequest()
        return apiService.addCartItem(request).toModel()
    }

    override suspend fun updateCartItemQuantity(updateCartItemQuantity: UpdateCartItemQuantity): Cart {
        val itemId = updateCartItemQuantity.itemId
        val request = updateCartItemQuantity.toRequest()
        return apiService.updateCartItemQuantity(itemId, request).toModel()
    }

    override suspend fun removeCartItem(itemId: String): Cart {
        return apiService.removeCartItem(itemId).toModel()
    }
}
