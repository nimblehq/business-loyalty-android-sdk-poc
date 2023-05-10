package co.nimblehq.loyalty.sdk

import android.content.Context
import android.content.Intent
import co.nimblehq.loyalty.sdk.api.mapError
import co.nimblehq.loyalty.sdk.exception.InitializationException
import co.nimblehq.loyalty.sdk.model.AddCartItem
import co.nimblehq.loyalty.sdk.model.AuthenticationState
import co.nimblehq.loyalty.sdk.model.Cart
import co.nimblehq.loyalty.sdk.model.Credentials
import co.nimblehq.loyalty.sdk.model.Order
import co.nimblehq.loyalty.sdk.model.OrderDetails
import co.nimblehq.loyalty.sdk.model.Product
import co.nimblehq.loyalty.sdk.model.RedeemReward
import co.nimblehq.loyalty.sdk.model.RedeemedReward
import co.nimblehq.loyalty.sdk.model.Reward
import co.nimblehq.loyalty.sdk.model.UpdateCartItemQuantity
import co.nimblehq.loyalty.sdk.network.NetworkBuilder
import co.nimblehq.loyalty.sdk.ui.authenticate.AuthenticationActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoyaltySdk private constructor() : NetworkBuilder() {

    companion object {
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

        fun init(context: Context, credentials: Credentials, isDebugMode: Boolean = false) {
            instance = LoyaltySdk().also { sdk ->
                context.let {
                    sdk.setContext(it)
                }

                with(credentials) {
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
                }

                sdk.setDebugMode(isDebugMode)
            }
        }
    }

    /*************
     ** REWARD **
     ************/

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

    /*************
     ** PRODUCT **
     ************/

    @DelicateCoroutinesApi
    fun getProductList(onResponse: (Result<List<Product>>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = productRepository.getProductList()
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
    fun getProductDetail(productId: String, onResponse: (Result<Product>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = productRepository.getProductDetail(productId)
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

    /**********
     ** CART **
     **********/

    @DelicateCoroutinesApi
    fun getCart(onResponse: (Result<Cart>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = cartRepository.getCart()
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
    fun addCartItem(addCartItem: AddCartItem, onResponse: (Result<Cart>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = cartRepository.addCartItem(addCartItem)
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
    fun updateCartItemQuantity(
        updateCartItemQuantity: UpdateCartItemQuantity,
        onResponse: (Result<Cart>) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = cartRepository.updateCartItemQuantity(updateCartItemQuantity)
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
    fun removeCartItem(
        itemId: String,
        onResponse: (Result<Cart>) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = cartRepository.removeCartItem(itemId)
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

    /*************
     ** ORDER **
     ************/

    @DelicateCoroutinesApi
    fun getOrderList(onResponse: (Result<List<Order>>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = orderRepository.getOrderList()
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
    fun getOrderDetail(orderId: String, onResponse: (Result<OrderDetails>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = orderRepository.getOrderDetail(orderId)
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
    fun submitOrder(cartId: String, onResponse: (Result<OrderDetails>) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = try {
                val result = orderRepository.submitOrder(cartId)
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
