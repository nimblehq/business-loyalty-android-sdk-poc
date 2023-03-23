package co.nimblehq.loyalty.sdk.model

import java.util.*

data class Reward(
    val id: String?,
    val organizationId: String?,
    val name: String?,
    val description: String?,
    val conditions: String?,
    val instructions: String?,
    val terms: String?,
    val type: String?,
    val state: String?,
    val expiresOn: Date?,
    val pointCost: Int?,
    val redeemedRewardsCount: Int?,
    val imageUrls: List<String>?,
)
