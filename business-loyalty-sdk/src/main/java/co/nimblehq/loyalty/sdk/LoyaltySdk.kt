package co.nimblehq.loyalty.sdk

import android.content.Context
import android.content.Intent
import co.nimblehq.loyalty.sdk.model.RedeemReward
import co.nimblehq.loyalty.sdk.model.RedeemedReward
import co.nimblehq.loyalty.sdk.model.Reward
import co.nimblehq.loyalty.sdk.network.NetworkBuilder
import co.nimblehq.loyalty.sdk.ui.authenticate.AuthenticationActivity
import kotlinx.coroutines.*

class LoyaltySdk private constructor() : NetworkBuilder() {

    companion object {
        val instance: LoyaltySdk by lazy { LoyaltySdk() }
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

    fun authenticate(context: Context) {
        with(context) {
            startActivity(
                Intent(this, AuthenticationActivity::class.java)
            )
        }
    }
}
