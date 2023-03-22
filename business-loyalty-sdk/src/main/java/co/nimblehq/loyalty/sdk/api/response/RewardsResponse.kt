package co.nimblehq.loyalty.sdk.api.response

import com.squareup.moshi.Json

data class RewardsResponse(
    @Json(name = "rewards")
    val rewards: List<RewardResponse>? = null
)

fun RewardsResponse.toModel() = this.rewards?.map { rewardResponse ->
    rewardResponse.toModel()
}
