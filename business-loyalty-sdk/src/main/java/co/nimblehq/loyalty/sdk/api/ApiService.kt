package co.nimblehq.loyalty.sdk.api

import co.nimblehq.loyalty.sdk.api.response.*
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

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
