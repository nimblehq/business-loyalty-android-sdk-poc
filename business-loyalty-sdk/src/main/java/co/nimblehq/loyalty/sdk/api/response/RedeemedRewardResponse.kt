package co.nimblehq.loyalty.sdk.api.response

import co.nimblehq.loyalty.sdk.model.RedeemedReward
import com.squareup.moshi.Json
import java.util.*

data class RedeemedRewardResponse(
    @Json(name = "id")
    val id: String?,
    @Json(name = "organization_id")
    val organizationId: String?,
    @Json(name = "customer_id")
    val customerId: String?,
    @Json(name = "reward_id")
    val rewardId: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "point_cost")
    val pointCost: Int?,
    @Json(name = "image_urls")
    val imageUrls: List<String>?,
    @Json(name = "created_at")
    val createdAt: Date?,
    @Json(name = "updated_at")
    val updatedAt: Date?,
    @Json(name = "reward")
    val reward: RewardResponse,
) {
    data class RewardResponse(
        @Json(name = "id")
        val id: String?,
        @Json(name = "organization_id")
        val organizationId: String?,
        @Json(name = "name")
        val name: String?,
        @Json(name = "description")
        val description: String?,
        @Json(name = "conditions")
        val conditions: String?,
        @Json(name = "instructions")
        val instructions: String?,
        @Json(name = "images")
        val images: RedeemedRewardImageResponse?,
        @Json(name = "terms")
        val terms: String?,
        @Json(name = "type")
        val type: String?,
        @Json(name = "status")
        val status: String?,
        @Json(name = "expires_on")
        val expiresOn: Date?,
        @Json(name = "point_cost")
        val pointCost: Int?,
        @Json(name = "deleted_at")
        val deletedAt: Date?,
        @Json(name = "created_at")
        val createdAt: Date?,
        @Json(name = "updated_at")
        val updatedAt: Date?,
        @Json(name = "redeemed_rewards_count")
        val redeemedRewardsCount: Int?,
    ) {
        data class RedeemedRewardImageResponse(
            @Json(name = "cdn_url")
            val cdnUrl: String?,
            @Json(name = "id")
            val id: String?,
            @Json(name = "files_count")
            val filesCount: String?,
        )
    }
}

fun List<RedeemedRewardResponse>.toModels() = this.map { response -> response.toModel() }

fun RedeemedRewardResponse.toModel() = RedeemedReward(
    id = id,
    organizationId = organizationId,
    customerId = customerId,
    rewardId = rewardId,
    status = status,
    pointCost = pointCost ?: 0,
    imageUrls = imageUrls,
    reward = reward.toModel()
)

fun RedeemedRewardResponse.RewardResponse.toModel() = RedeemedReward.Reward(
    id = id,
    organizationId = organizationId,
    name = name,
    description = description,
    conditions = conditions,
    instructions = instructions,
    images = images?.toModel(),
    terms = terms,
    type = type,
    status = status,
    expiresOn = expiresOn,
    pointCost = pointCost,
    redeemedRewardsCount = redeemedRewardsCount,
)

fun RedeemedRewardResponse.RewardResponse.RedeemedRewardImageResponse.toModel() =
    RedeemedReward.Reward.Images(
        cdnUrl = cdnUrl,
        id = id,
        filesCount = filesCount,
    )
