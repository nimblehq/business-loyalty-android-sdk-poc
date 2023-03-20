package co.nimblehq.loyalty.sdk.api.response

import co.nimblehq.loyalty.sdk.model.Reward
import com.squareup.moshi.Json
import java.util.*

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
    @Json(name = "terms")
    var terms: String?,
    @Json(name = "type")
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
    @Json(name = "image_urls")
    var imageUrls: List<String>?,
)

fun RewardResponse.toModel() = Reward(
    id = id.orEmpty(),
    organizationId = organizationId.orEmpty(),
    name = name.orEmpty(),
    description = description.orEmpty(),
    conditions = conditions.orEmpty(),
    instructions = instructions.orEmpty(),
    terms = terms.orEmpty(),
    state = state.orEmpty(),
    expiresOn = expiresOn ?: Calendar.getInstance().time,
    pointCost = pointCost ?: 0,
    redeemedRewardsCount = redeemedRewardsCount ?: 0,
    imageUrls = imageUrls.orEmpty(),
)
