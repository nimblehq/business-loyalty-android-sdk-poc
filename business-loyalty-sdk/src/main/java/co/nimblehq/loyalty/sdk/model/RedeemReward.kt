package co.nimblehq.loyalty.sdk.model

import java.util.*

data class RedeemReward(
    private val id: String,
    private val organizationId: String,
    private val name: String,
    private val description: String,
    private val conditions: String,
    private val instructions: String,
    private val terms: String,
    private val state: String,
    private val expiresOn: Date,
    private val pointCost: Int,
    private val redeemedRewardsCount: Int,
    private val imageUrls: List<String>,
)
