package co.nimblehq.loyalty.sdk.repository

import co.nimblehq.loyalty.sdk.api.ApiService
import co.nimblehq.loyalty.sdk.api.response.toModel
import co.nimblehq.loyalty.sdk.model.Reward

internal interface RewardRepository {
    suspend fun getReward(): List<Reward>
}

internal class RewardRepositoryImpl(
    private val apiService: ApiService
): RewardRepository {

    override suspend fun getReward(): List<Reward> {
        return apiService.getRewards().toModel()
    }
}