package co.nimblehq.loyalty.sdk.model

import java.util.*

data class RedeemedReward(
    val id: String? = null,
    val organizationId: String? = null,
    val customerId: String? = null,
    val rewardId: String? = null,
    val state: String? = null,
    val pointCost: Int? = null,
    val images: String? = null,
    val reward: Reward? = null,
) {
    data class Reward(
        val id: String? = null,
        val organizationId: String? = null,
        val name: String? = null,
        val description: String? = null,
        val conditions: String? = null,
        val instructions: String? = null,
        val images: Images? = null,
        val terms: String? = null,
        val type: String? = null,
        val state: String? = null,
        val expiresOn: Date? = null,
        val pointCost: Int? = null,
        val redeemedRewardsCount: Int? = null,
    ) {
        data class Images(
            val cdnUrl: String? = null,
            val id: String? = null,
            val filesCount: String? = null,
        )
    }
}
