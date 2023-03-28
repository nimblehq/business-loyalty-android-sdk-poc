package co.nimblehq.loyalty.sdk.model

import java.util.*

data class RedeemReward(
    val id: String?,
    val organizationId: String?,
    val customerId: String?,
    val rewardId: String?,
    val status: String?,
    val pointCost: Int?,
    val imageUrls: List<String>?,
    val reward: Reward?,
) {
    data class Reward(
        val id: String?,
        val organizationId: String?,
        val name: String?,
        val description: String?,
        val conditions: String?,
        val instructions: String?,
        val terms: String?,
        val type: String?,
        val status: String?,
        val expiresOn: Date?,
        val pointCost: Int?,
        val redeemedRewardsCount: Int?,
    )
}
