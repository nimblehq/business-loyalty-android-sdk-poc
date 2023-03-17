package co.nimblehq.loyalty.sdk.model

data class Reward(
    private val id: String,
    private val organizationId: String,
    private val name: String,
    private val description: String,
    private val conditions: String,
    private val instructions: String,
    private val terms: String,
    private val state: String,
    private val expiresOn: String?, // TODO Convert to Date
    private val pointCost: Int,
    private val deletedAt: String?,
    private val createdAt: String?,
    private val updatedAt: String?,
    private val redeemedRewardsCount: Int,
    private val imageUrls: List<String>,
)
