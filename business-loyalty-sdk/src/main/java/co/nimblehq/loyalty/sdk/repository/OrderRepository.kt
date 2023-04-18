package co.nimblehq.loyalty.sdk.repository

import co.nimblehq.loyalty.sdk.api.ApiService
import co.nimblehq.loyalty.sdk.api.response.toModel
import co.nimblehq.loyalty.sdk.api.response.toModels
import co.nimblehq.loyalty.sdk.model.Order
import co.nimblehq.loyalty.sdk.model.OrderDetails

internal interface OrderRepository {
    suspend fun getOrderList(): List<Order>
    suspend fun getOrderDetail(orderId: String): OrderDetails
    suspend fun submitOrder(cartId: String): OrderDetails
}

internal class OrderRepositoryImpl(
    private val apiService: ApiService
) : OrderRepository {

    override suspend fun getOrderList(): List<Order> {
        return apiService.getOrders().toModels()
    }

    override suspend fun getOrderDetail(orderId: String): OrderDetails {
        return apiService.getOrderDetail(orderId).toModel()
    }

    override suspend fun submitOrder(cartId: String): OrderDetails {
        return apiService.submitOrder(cartId).toModel()
    }
}
