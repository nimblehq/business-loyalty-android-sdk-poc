package co.nimblehq.loyalty.sdk.repository

import co.nimblehq.loyalty.sdk.api.ApiService
import co.nimblehq.loyalty.sdk.api.response.toModels
import co.nimblehq.loyalty.sdk.model.Order

internal interface OrderRepository {
    suspend fun getOrderList(): List<Order>
}

internal class OrderRepositoryImpl(
    private val apiService: ApiService
) : OrderRepository {

    override suspend fun getOrderList(): List<Order> {
        return apiService.getOrders().toModels()
    }
}
