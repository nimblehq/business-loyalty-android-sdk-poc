package co.nimblehq.loyalty.sdk.api.response

import com.squareup.moshi.Json

data class RewardsResponse(
    @Json(name = "rewards")
    var rewards: List<RewardResponse>
)

fun RewardsResponse.toModel() = this.rewards.map { rewardResponse ->
    rewardResponse.toModel()
}
