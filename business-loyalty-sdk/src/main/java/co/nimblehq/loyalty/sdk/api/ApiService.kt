package co.nimblehq.loyalty.sdk.api

import co.nimblehq.loyalty.sdk.api.response.RewardsResponse
import retrofit2.http.GET

interface ApiService {
    @GET("/rewards.json")
    suspend fun getRewards(): RewardsResponse
}