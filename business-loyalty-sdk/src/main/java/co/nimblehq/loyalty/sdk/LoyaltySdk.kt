package co.nimblehq.loyalty.sdk

import android.content.Context
import android.content.Intent
import co.nimblehq.loyalty.sdk.api.request.BaseRequest
import co.nimblehq.loyalty.sdk.api.request.Credentials
import co.nimblehq.loyalty.sdk.model.Reward
import co.nimblehq.loyalty.sdk.network.NetworkBuilder
import co.nimblehq.loyalty.sdk.ui.authenticate.AuthenticationActivity
import kotlinx.coroutines.*

class LoyaltySdk private constructor() : NetworkBuilder() {

    companion object {
        val instance: LoyaltySdk by lazy { LoyaltySdk() }
    }

    fun withMode(debugMode: Boolean): LoyaltySdk {
        setDebugMode(debugMode)
        return this
    }

    fun withBaseUrl(url: String): LoyaltySdk {
        setBaseUrl(url)
        return this
    }

    fun withBaseAuthorizationUrl(url: String): LoyaltySdk {
        setBaseAuthorizationUrl(url)
        return this
    }

    fun withCredentials(credentials: Credentials) {
        BaseRequest.updateAuthenticationInfo(credentials)
    }

    fun setAccessToken(token: String): LoyaltySdk {
        super.setToken(token)
        return this
    }

    fun withConnectionTimeout(timeout: Long): LoyaltySdk {
        setConnectionTimeoutInSecond(timeout)
        return this
    }

    fun withReadTimeout(timeout: Long): LoyaltySdk {
        setReadTimeoutInSecond(timeout)
        return this
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

    fun authenticate(context: Context) {
        with(context) {
            startActivity(
                Intent(this, AuthenticationActivity::class.java)
            )
        }
    }
}
