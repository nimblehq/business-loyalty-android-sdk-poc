package co.nimblehq.loyalty.sdk.repository

import co.nimblehq.loyalty.sdk.model.Reward

interface RewardRepository {
    fun getReward(): List<Reward>
}

class RewardRepositoryImpl(

): RewardRepository {
    override fun getReward(): List<Reward> {
        TODO("Not yet implemented")
    }
}