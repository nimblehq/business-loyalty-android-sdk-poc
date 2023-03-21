package co.nimblehq.loyalty.sdk.repository

import co.nimblehq.loyalty.sdk.api.ApiService
import co.nimblehq.loyalty.sdk.api.response.toModel
import co.nimblehq.loyalty.sdk.model.RedeemReward
import co.nimblehq.loyalty.sdk.model.Reward

internal interface RewardRepository {
    suspend fun getReward(): List<Reward>

    suspend fun redeemReward(rewardId: String): RedeemReward
}

internal class RewardRepositoryImpl(
    private val apiService: ApiService
): RewardRepository {

    override suspend fun getReward(): List<Reward> {
        return apiService.getRewards().toModel()
    }

    override suspend fun redeemReward(rewardId: String): RedeemReward {
        return apiService.redeemReward(rewardId).toModel()
    }
}