package co.nimblehq.loyalty.sdk.poc.data.repository

import co.nimblehq.loyalty.sdk.LoyaltySdk
import co.nimblehq.loyalty.sdk.Result
import co.nimblehq.loyalty.sdk.model.*
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
    fun getRewardDetail(rewardId: String): Flow<Reward>
    fun redeemReward(rewardId: String): Flow<RedeemReward>
    fun clearSession(): Flow<Unit>

    val products: Flow<List<Product>>
    fun getProductDetail(productId: String): Flow<Product>

    val cart: Flow<Cart>
    fun addCartItem(addCartItem: AddCartItem): Flow<Cart>
    fun removeCartItem(itemId: String): Flow<Cart>
}

@OptIn(DelicateCoroutinesApi::class)
class SampleAppRepositoryImpl @Inject constructor() : SampleAppRepository {

    override val rewards: Flow<List<Reward>> = flow {
        val result = getRewardListFromSdk()
        emit(result)
    }

    override val redeemedRewards: Flow<List<RedeemedReward>>
        get() = flow {
            val result = getRedeemedRewardListFromSdk()
            emit(result)
        }

    override val isAuthenticated: Flow<Boolean>
        get() = flow {
            val result = getAuthenticationStateFromSdk()
            emit(result == AuthenticationState.Authenticated)
        }

    override fun getRewardDetail(rewardId: String): Flow<Reward> = flow {
        val result = getRewardDetailFromSdk(rewardId)
        emit(result)
    }

    override fun redeemReward(rewardId: String): Flow<RedeemReward> = flow {
        val result = redeemRewardByIdFromSdk(rewardId)
        emit(result)
    }

    override fun clearSession(): Flow<Unit> = flow {
        val result = clearSessionStateFromSdk()
        emit(result)
    }

    override val products: Flow<List<Product>> = flow {
        val result = getProductListFromSdk()
        emit(result)
    }

    override fun getProductDetail(productId: String): Flow<Product> {
        return flow {
            val result = getProductDetailFromSdk(productId)
            emit(result)
        }
    }

    override val cart: Flow<Cart> = flow {
        val result = getCartFromSdk()
        emit(result)
    }

    override fun addCartItem(addCartItem: AddCartItem) = flow {
        val result = addCartItemFromSdk(addCartItem)
        emit(result)
    }

    override fun removeCartItem(itemId: String) = flow {
        val result = removeCartItemFromSdk(itemId)
        emit(result)
    }

    private suspend fun getRewardListFromSdk() = suspendCoroutine { continuation ->
        LoyaltySdk.getInstance().getRewardList { result ->
            when (result) {
                is Result.Success -> continuation.resume(result.data)
                is Result.Error -> continuation.resumeWithException(result.exception)
                else -> {}
            }
        }
    }

    private suspend fun getRedeemedRewardListFromSdk() = suspendCoroutine { continuation ->
        LoyaltySdk.getInstance().getRedeemedRewardList { result ->
            when (result) {
                is Result.Success -> continuation.resume(result.data)
                is Result.Error -> continuation.resumeWithException(result.exception)
                else -> {}
            }
        }
    }

    private suspend fun getAuthenticationStateFromSdk() = suspendCoroutine { continuation ->
        LoyaltySdk.getInstance().getAuthenticationState { result ->
            when (result) {
                is Result.Success -> continuation.resume(result.data)
                is Result.Error -> continuation.resumeWithException(result.exception)
                else -> {}
            }
        }
    }

    private suspend fun getRewardDetailFromSdk(rewardId: String) =
        suspendCoroutine { continuation ->
            LoyaltySdk.getInstance().getRewardDetail(rewardId) { result ->
                when (result) {
                    is Result.Success -> continuation.resume(result.data)
                    is Result.Error -> continuation.resumeWithException(result.exception)
                    else -> {}
                }
            }
        }

    private suspend fun redeemRewardByIdFromSdk(rewardId: String) =
        suspendCoroutine { continuation ->
            LoyaltySdk.getInstance().redeemReward(rewardId) { result ->
                when (result) {
                    is Result.Success -> continuation.resume(result.data)
                    is Result.Error -> continuation.resumeWithException(result.exception)
                    else -> {}
                }
            }
        }

    private suspend fun clearSessionStateFromSdk() = suspendCoroutine { continuation ->
        LoyaltySdk.getInstance().clearSession { result ->
            when (result) {
                is Result.Success -> continuation.resume(result.data)
                is Result.Error -> continuation.resumeWithException(result.exception)
                else -> {}
            }
        }
    }

    private suspend fun getProductListFromSdk() = suspendCoroutine { continuation ->
        LoyaltySdk.getInstance().getProductList { result ->
            when (result) {
                is Result.Success -> continuation.resume(result.data)
                is Result.Error -> continuation.resumeWithException(result.exception)
                else -> {}
            }
        }
    }

    private suspend fun getProductDetailFromSdk(productId: String) =
        suspendCoroutine { continuation ->
            LoyaltySdk.getInstance().getProductDetail(productId) { result ->
                when (result) {
                    is Result.Success -> continuation.resume(result.data)
                    is Result.Error -> continuation.resumeWithException(result.exception)
                    else -> {}
                }
            }
        }

    private suspend fun getCartFromSdk() =
        suspendCoroutine { continuation ->
            LoyaltySdk.getInstance().getCart { result ->
                when (result) {
                    is Result.Success -> continuation.resume(result.data)
                    is Result.Error -> continuation.resumeWithException(result.exception)
                    else -> {}
                }
            }
        }

    private suspend fun addCartItemFromSdk(addCartItem: AddCartItem) =
        suspendCoroutine { continuation ->
            LoyaltySdk.getInstance().addCartItem(addCartItem) { result ->
                when (result) {
                    is Result.Success -> continuation.resume(result.data)
                    is Result.Error -> continuation.resumeWithException(result.exception)
                    else -> {}
                }
            }
        }

    private suspend fun removeCartItemFromSdk(itemId: String) = suspendCoroutine { continuation ->
        LoyaltySdk.getInstance().removeCartItem(itemId) { result ->
            when (result) {
                is Result.Success -> continuation.resume(result.data)
                is Result.Error -> continuation.resumeWithException(result.exception)
                else -> {}
            }
        }
    }
}