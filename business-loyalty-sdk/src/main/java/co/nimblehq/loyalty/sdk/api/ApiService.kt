package co.nimblehq.loyalty.sdk.api

import co.nimblehq.loyalty.sdk.api.response.RedeemRewardResponse
import co.nimblehq.loyalty.sdk.api.response.RewardsResponse
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ApiService {
    @GET("/rewards.json")
    suspend fun getRewards(): RewardsResponse

    @PATCH("/rewards/{reward_id}/redeem.json")
    suspend fun redeemReward(
        @Path("reward_id") rewardId: String
    ): RedeemRewardResponse
}
