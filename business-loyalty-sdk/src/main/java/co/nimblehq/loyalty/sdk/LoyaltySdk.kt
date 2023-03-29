package co.nimblehq.loyalty.sdk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import co.nimblehq.loyalty.sdk.api.mapError
import co.nimblehq.loyalty.sdk.exception.InitializationException
import co.nimblehq.loyalty.sdk.model.AuthenticationState
import co.nimblehq.loyalty.sdk.model.RedeemReward
import co.nimblehq.loyalty.sdk.model.RedeemedReward
import co.nimblehq.loyalty.sdk.model.Reward
import co.nimblehq.loyalty.sdk.network.NetworkBuilder
import co.nimblehq.loyalty.sdk.ui.authenticate.AuthenticationActivity
import kotlinx.coroutines.*

class LoyaltySdk private constructor() : NetworkBuilder() {

    companion object {
        // FIXME Revise this warning
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: LoyaltySdk

        @JvmStatic
        fun getInstance(): LoyaltySdk {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    throw InitializationException.LateInitialization
                }
                return instance
            }
        }
    }

    class Builder {

        // FIXME Revise this warning
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null
        private var isDebugMode: Boolean = false
        private var clientId: String = ""
        private var clientSecret: String = ""

        fun withContext(context: Context) = apply { this.context = context.applicationContext }

        fun withDebugMode(isDebugMode: Boolean) = apply { this.isDebugMode = isDebugMode }

        fun withClientId(clientId: String) = apply { this.clientId = clientId }

        fun withClientSecret(clientSecret: String) = apply { this.clientSecret = clientSecret }

        fun init() {
            instance = LoyaltySdk().also { sdk ->
                context?.let {
                    sdk.setContext(it)
                } ?: throw InitializationException.InvalidContext

                if (clientId.isEmpty()) {
                    throw InitializationException.InvalidClientId
                } else {
                    sdk.setClientId(clientId)
                }

                if (clientSecret.isEmpty()) {
                    throw InitializationException.InvalidClientSecret
                } else {
                    sdk.setClientSecret(clientSecret)
                }

                sdk.setDebugMode(isDebugMode)
            }
        }
    }

    @DelicateCoroutinesApi
    fun getRewardList(onResponse: (Result<List<Reward>>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = rewardRepository.getRewardList()
                Result.Success(result)
            } catch (exception: Exception) {
                exception.printStackTrace()
                Result.Error(exception.mapError())
            }
            withContext(Dispatchers.Main) {
                onResponse(result)
            }
        }
    }

    @DelicateCoroutinesApi
    fun getRewardDetail(rewardId: String, onResponse: (Result<Reward>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = rewardRepository.getRewardDetail(rewardId)
                Result.Success(result)
            } catch (exception: Exception) {
                exception.printStackTrace()
                Result.Error(exception.mapError())
            }
            withContext(Dispatchers.Main) {
                onResponse(result)
            }
        }
    }

    @DelicateCoroutinesApi
    fun redeemReward(rewardId: String, onResponse: (Result<RedeemReward>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = rewardRepository.redeemReward(rewardId)
                Result.Success(result)
            } catch (exception: Exception) {
                exception.printStackTrace()
                Result.Error(exception.mapError())
            }
            withContext(Dispatchers.Main) {
                onResponse(result)
            }
        }
    }

    @DelicateCoroutinesApi
    fun getRedeemedRewardList(onResponse: (Result<List<RedeemedReward>>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = rewardRepository.getRedeemedRewardList()
                Result.Success(result)
            } catch (exception: Exception) {
                exception.printStackTrace()
                Result.Error(exception.mapError())
            }
            withContext(Dispatchers.Main) {
                onResponse(result)
            }
        }
    }

    @DelicateCoroutinesApi
    fun getAuthenticationState(onResponse: (Result<AuthenticationState>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = authenticationRepository.getAuthenticationState()
                Result.Success(result)
            } catch (exception: Exception) {
                exception.printStackTrace()
                Result.Error(exception.mapError())
            }
            withContext(Dispatchers.Main) {
                onResponse(result)
            }
        }
    }

    fun authenticate(activityContext: Context) {
        with(activityContext) {
            startActivity(
                Intent(this, AuthenticationActivity::class.java)
            )
        }
    }

    @DelicateCoroutinesApi
    fun clearSession(onResponse: (Result<Unit>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = authenticationRepository.clearAuthenticateState()
                Result.Success(result)
            } catch (exception: Exception) {
                exception.printStackTrace()
                Result.Error(exception.mapError())
            }
            withContext(Dispatchers.Main) {
                onResponse(result)
            }
        }
    }
}
