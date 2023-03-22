package co.nimblehq.loyalty.sdk.api.response

import co.nimblehq.loyalty.sdk.model.RedeemedReward
import com.squareup.moshi.Json
import java.util.*

data class RedeemedRewardResponse(
    @Json(name = "id")
    var id: String?,
    @Json(name = "organization_id")
    var organizationId: String?,
    @Json(name = "customer_id")
    var customerId: String?,
    @Json(name = "reward_id")
    var rewardId: String?,
    @Json(name = "state")
    var state: String?,
    @Json(name = "point_cost")
    var pointCost: Int?,
    @Json(name = "images")
    var images: String?,
    @Json(name = "created_at")
    var createdAt: Date?,
    @Json(name = "updated_at")
    var updatedAt: Date?,
    @Json(name = "reward")
    var reward: RewardResponse,
) {
    data class RewardResponse(
        @Json(name = "id")
        var id: String?,
        @Json(name = "organization_id")
        var organizationId: String?,
        @Json(name = "name")
        var name: String?,
        @Json(name = "description")
        var description: String?,
        @Json(name = "conditions")
        var conditions: String?,
        @Json(name = "instructions")
        var instructions: String?,
        @Json(name = "images")
        var images: RedeemedRewardImageResponse?,
        @Json(name = "terms")
        var terms: String?,
        @Json(name = "type")
        var type: String?,
        @Json(name = "state")
        var state: String?,
        @Json(name = "expires_on")
        var expiresOn: Date?,
        @Json(name = "point_cost")
        var pointCost: Int?,
        @Json(name = "deleted_at")
        var deletedAt: Date?,
        @Json(name = "created_at")
        var createdAt: Date?,
        @Json(name = "updated_at")
        var updatedAt: Date?,
        @Json(name = "redeemed_rewards_count")
        var redeemedRewardsCount: Int?,
    ) {
        data class RedeemedRewardImageResponse(
            @Json(name = "cdn_url")
            var cdnUrl: String?,
            @Json(name = "id")
            var id: String?,
            @Json(name = "files_count")
            var filesCount: String?,
        )
    }
}

fun List<RedeemedRewardResponse>.toModels() = this.map { response -> response.toModel() }

fun RedeemedRewardResponse.toModel() = RedeemedReward(
    id = id,
    organizationId = organizationId,
    customerId = customerId,
    rewardId = rewardId,
    state = state,
    pointCost = pointCost ?: 0,
    images = images,
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
)

fun RedeemedRewardResponse.RewardResponse.RedeemedRewardImageResponse.toModel() =
    RedeemedReward.Reward.Images(
        cdnUrl = cdnUrl,
        id = id,
        filesCount = filesCount,
    )
