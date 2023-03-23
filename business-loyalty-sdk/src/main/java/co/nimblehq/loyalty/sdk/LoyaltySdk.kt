package co.nimblehq.loyalty.sdk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import co.nimblehq.loyalty.sdk.model.RedeemReward
import co.nimblehq.loyalty.sdk.model.RedeemedReward
import co.nimblehq.loyalty.sdk.model.Reward
import co.nimblehq.loyalty.sdk.network.NetworkBuilder
import co.nimblehq.loyalty.sdk.ui.authenticate.AuthenticationActivity
import kotlinx.coroutines.*

class LoyaltySdk private constructor() : NetworkBuilder() {

    companion object Builder {
        // FIXME Revise this warning
        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: LoyaltySdk

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
                context?.let{
                    sdk.setContext(it)
                } ?: throw RuntimeException("Context must not be null")
                sdk.setClientId(clientId)
                sdk.setClientSecret(clientSecret)
                sdk.setDebugMode(isDebugMode)
            }
        }

        fun getInstance(): LoyaltySdk {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    throw RuntimeException("LoyaltySdk has not been initialized. Initialize new instance by using LoyaltySdk.Builder")
                }
                return instance
            }
        }
    }

    @DelicateCoroutinesApi
    fun getRewards(onResponse: (Result<List<Reward>>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = rewardRepository.getReward()
                Result.Success(result)
            } catch (exception: Exception) {
                exception.printStackTrace()
                Result.Error(exception)
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
                Result.Error(exception)
            }
            withContext(Dispatchers.Main) {
                onResponse(result)
            }
        }
    }

    @DelicateCoroutinesApi
    fun getRedeemedReward(onResponse: (Result<List<RedeemedReward>>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = rewardRepository.getRedeemedReward()
                Result.Success(result)
            } catch (exception: Exception) {
                exception.printStackTrace()
                Result.Error(exception)
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
}
