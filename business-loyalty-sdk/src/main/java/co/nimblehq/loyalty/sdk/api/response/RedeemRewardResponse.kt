package co.nimblehq.loyalty.sdk.api.response

import co.nimblehq.loyalty.sdk.model.RedeemReward
import com.squareup.moshi.Json
import java.util.*

data class RedeemRewardResponse(
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "organization_id")
    val organizationId: String? = null,
    @Json(name = "customer_id")
    val customerId: String? = null,
    @Json(name = "reward_id")
    val rewardId: String? = null,
    @Json(name = "state")
    val state: String? = null,
    @Json(name = "point_cost")
    val pointCost: Int? = null,
    @Json(name = "created_at")
    val createdAt: Date? = null,
    @Json(name = "updated_at")
    val updatedAt: Date? = null,
    @Json(name = "image_urls")
    val imageUrls: List<String>? = null,
    @Json(name = "reward")
    val reward: RewardResponse? = null,
) {
    data class RewardResponse(
        @Json(name = "id")
        val id: String? = null,
        @Json(name = "organization_id")
        val organizationId: String? = null,
        @Json(name = "name")
        val name: String? = null,
        @Json(name = "description")
        val description: String? = null,
        @Json(name = "conditions")
        val conditions: String? = null,
        @Json(name = "instructions")
        val instructions: String? = null,
        // TODO Skip this field
//        @Json(name = "images")
//        val images: String? = null,
        @Json(name = "terms")
        val terms: String? = null,
        @Json(name = "type")
        val type: String? = null,
        @Json(name = "state")
        val state: String? = null,
        @Json(name = "expires_on")
        val expiresOn: Date? = null,
        @Json(name = "point_cost")
        val pointCost: Int? = null,
        @Json(name = "deleted_at")
        val deletedAt: Date? = null,
        @Json(name = "created_at")
        val createdAt: Date? = null,
        @Json(name = "updated_at")
        val updatedAt: Date? = null,
        @Json(name = "redeemed_rewards_count")
        val redeemedRewardsCount: Int? = null,
    )
}

fun RedeemRewardResponse.toModel() = RedeemReward(
    id = id,
    organizationId = organizationId,
    customerId = customerId,
    rewardId = rewardId,
    state = state,
    pointCost = pointCost,
    imageUrls = imageUrls,
    reward = reward?.toModel()
)

fun RedeemRewardResponse.RewardResponse.toModel() = RedeemReward.Reward(
    id = id,
    organizationId = organizationId,
    name = name,
    description = description,
    conditions = conditions,
    instructions = instructions,
    terms = terms,
    type = type,
    state = state,
    expiresOn = expiresOn,
    pointCost = pointCost,
    redeemedRewardsCount = redeemedRewardsCount,
)
