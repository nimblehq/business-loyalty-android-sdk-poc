package co.nimblehq.loyalty.sdk.poc.data.repository

import co.nimblehq.loyalty.sdk.LoyaltySdk
import co.nimblehq.loyalty.sdk.Result
import co.nimblehq.loyalty.sdk.model.AuthenticationState
import co.nimblehq.loyalty.sdk.model.RedeemReward
import co.nimblehq.loyalty.sdk.model.RedeemedReward
import co.nimblehq.loyalty.sdk.model.Reward
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface SampleAppRepository {
    val rewards: Flow<List<Reward>>
    val redeemedRewards: Flow<List<RedeemedReward>>
    val isAuthenticated: Flow<Boolean>
    fun redeemReward(rewardId: String): Flow<RedeemReward>
    fun clearSession(): Flow<Unit>
}

class SampleAppRepositoryImpl @Inject constructor() : SampleAppRepository {

    override val rewards: Flow<List<Reward>> = flow {
            val result = getRewardsFromSdk()
            emit(result)
        }

    override val redeemedRewards: Flow<List<RedeemedReward>>
        get() = flow {
            val result = getRedeemedRewardFromSdk()
            emit(result)
        }

    override val isAuthenticated: Flow<Boolean>
        get() = flow {
            val result = getAuthenticationStateFromSdk()
            emit(result == AuthenticationState.Authenticated)
        }

    override fun redeemReward(rewardId: String): Flow<RedeemReward> = flow {
        val result = redeemRewardByIdFromSdk(rewardId)
        emit(result)
    }

    override fun clearSession(): Flow<Unit> = flow {
            val result = clearAuthenticationStateFromSdk()
            emit(result)
        }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun getRewardsFromSdk() = suspendCoroutine { continuation ->
        LoyaltySdk.getInstance().getRewards { result ->
            when (result) {
                is Result.Success -> continuation.resume(result.data)
                is Result.Error -> continuation.resumeWithException(result.exception)
                else -> {}
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun getRedeemedRewardFromSdk() = suspendCoroutine { continuation ->
        LoyaltySdk.getInstance().getRedeemedReward { result ->
            when (result) {
                is Result.Success -> continuation.resume(result.data)
                is Result.Error -> continuation.resumeWithException(result.exception)
                else -> {}
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun getAuthenticationStateFromSdk() = suspendCoroutine { continuation ->
        LoyaltySdk.getInstance().getAuthenticationState { result ->
            when (result) {
                is Result.Success -> continuation.resume(result.data)
                is Result.Error -> continuation.resumeWithException(result.exception)
                else -> {}
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun redeemRewardByIdFromSdk(rewardId: String) = suspendCoroutine { continuation ->
        LoyaltySdk.getInstance().redeemReward(rewardId) { result ->
            when (result) {
                is Result.Success -> continuation.resume(result.data)
                is Result.Error -> continuation.resumeWithException(result.exception)
                else -> {}
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun clearAuthenticationStateFromSdk() = suspendCoroutine { continuation ->
        LoyaltySdk.getInstance().clearAuthenticateState { result ->
            when (result) {
                is Result.Success -> continuation.resume(result.data)
                is Result.Error -> continuation.resumeWithException(result.exception)
                else -> {}
            }
        }
    }
}