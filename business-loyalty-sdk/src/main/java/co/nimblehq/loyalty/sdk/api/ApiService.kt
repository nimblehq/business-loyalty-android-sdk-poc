package co.nimblehq.loyalty.sdk.api

import co.nimblehq.loyalty.sdk.api.request.AddCartItemRequest
import co.nimblehq.loyalty.sdk.api.request.UpdateCartItemQuantityRequest
import co.nimblehq.loyalty.sdk.api.response.*
import retrofit2.http.*

interface ApiService {
    /*************
     ** REWARD **
     ************/

    @GET("rewards.json")
    suspend fun getRewards(): RewardsResponse

    @GET("rewards/{reward_id}.json")
    suspend fun getRewardDetail(
        @Path("reward_id") rewardId: String
    ): RewardResponse

    @PATCH("rewards/{reward_id}/redeem.json")
    suspend fun redeemReward(
        @Path("reward_id") rewardId: String
    ): RedeemRewardResponse

    @GET("redeemed_rewards.json")
    suspend fun getRedeemedRewards(): List<RedeemedRewardResponse>

    /*************
     ** PRODUCT **
     ************/

    @GET("products.json")
    suspend fun getProducts(): List<ProductResponse>

    @GET("products/{product_id}.json")
    suspend fun getProductDetail(
        @Path("product_id") productId: String
    ): ProductResponse

    /***********
     ** CART **
     ***********/

    @GET("cart.json")
    suspend fun getCart(): CartResponse

    @POST("order_line_items.json")
    suspend fun addCartItem(
        @Body request: AddCartItemRequest
    ): CartResponse

    @PATCH("order_line_items/{item_id}.json")
    suspend fun updateCartItemQuantity(
        @Path("item_id") itemId: String,
        @Body request: UpdateCartItemQuantityRequest
    ): CartResponse

    @DELETE("order_line_items/{item_id}.json")
    suspend fun removeCartItem(
        @Path("item_id") itemId: String,
    ): CartResponse

    /***********
     ** ORDER **
     ***********/

    @GET("orders.json")
    suspend fun getOrders(): List<OrderResponse>

    @GET("orders/{order_id}.json")
    suspend fun getOrderDetail(
        @Path("order_id") orderId: String
    ): OrderDetailsResponse

    @POST("orders/{cart_id}/submit.json")
    suspend fun submitOrder(
        @Path("cart_id") cartId: String
    ): OrderDetailsResponse
}
