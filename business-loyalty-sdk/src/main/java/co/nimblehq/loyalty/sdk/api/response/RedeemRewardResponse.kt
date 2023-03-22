package co.nimblehq.loyalty.sdk.api.response

import co.nimblehq.loyalty.sdk.model.RedeemReward
import com.squareup.moshi.Json
import java.util.*

data class RedeemRewardResponse(
    @Json(name = "id")
    var id: String?,
    @Json(name = "organization_id")
    var organizationId: String?,
    @Json(name = "customer_id")
    var customerId: String?,
    @Json(name = "reward_id")
    var rewardId: String?,
    @Json(name = "type")
    var state: String?,
    @Json(name = "point_cost")
    var pointCost: Int?,
    @Json(name = "image_urls")
    var imageUrls: List<String>?,
    @Json(name = "reward")
    var reward: RewardResponse,
)

fun RedeemRewardResponse.toModel() = RedeemReward(
    id = id.orEmpty(),
    organizationId = organizationId.orEmpty(),
    name = reward.name.orEmpty(),
    description = reward.description.orEmpty(),
    conditions = reward.conditions.orEmpty(),
    instructions = reward.instructions.orEmpty(),
    terms = reward.terms.orEmpty(),
    state = state.orEmpty(),
    expiresOn = reward.expiresOn ?: Calendar.getInstance().time,
    pointCost = pointCost ?: 0,
    redeemedRewardsCount = reward.redeemedRewardsCount ?: 0,
    imageUrls = imageUrls.orEmpty(),
)
