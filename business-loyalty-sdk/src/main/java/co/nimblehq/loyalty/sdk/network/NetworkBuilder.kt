package co.nimblehq.loyalty.sdk.network

import android.content.Context
import co.nimblehq.loyalty.sdk.BuildConfig
import co.nimblehq.loyalty.sdk.api.ApiService
import co.nimblehq.loyalty.sdk.api.AuthenticationService
import co.nimblehq.loyalty.sdk.api.interceptor.AuthorizationInterceptor
import co.nimblehq.loyalty.sdk.exception.InitializationException
import co.nimblehq.loyalty.sdk.persistence.AuthPersistence
import co.nimblehq.loyalty.sdk.persistence.PersistenceProvider
import co.nimblehq.loyalty.sdk.repository.AuthenticationRepository
import co.nimblehq.loyalty.sdk.repository.AuthenticationRepositoryImpl
import co.nimblehq.loyalty.sdk.repository.CartRepository
import co.nimblehq.loyalty.sdk.repository.CartRepositoryImpl
import co.nimblehq.loyalty.sdk.repository.OrderRepository
import co.nimblehq.loyalty.sdk.repository.OrderRepositoryImpl
import co.nimblehq.loyalty.sdk.repository.ProductRepository
import co.nimblehq.loyalty.sdk.repository.ProductRepositoryImpl
import co.nimblehq.loyalty.sdk.repository.RewardRepository
import co.nimblehq.loyalty.sdk.repository.RewardRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT_IN_SECOND = 30L
private const val READ_TIMEOUT_IN_SECOND = 30L

abstract class NetworkBuilder {
    private val authPersistence: AuthPersistence by lazy {
        PersistenceProvider.getAuthPersistence(
            context.get() ?: throw InitializationException.InvalidContext
        )
    }
    private val apiService: ApiService by lazy { buildService() }
    private val authenticationService: AuthenticationService by lazy { buildAuthenticationService() }

    internal val rewardRepository: RewardRepository by lazy { RewardRepositoryImpl(apiService) }
    internal val authenticationRepository: AuthenticationRepository by lazy {
        AuthenticationRepositoryImpl(authenticationService, authPersistence)
    }
    internal val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl(apiService)
    }
    internal val orderRepository: OrderRepository by lazy {
        OrderRepositoryImpl(apiService)
    }
    internal val cartRepository: CartRepository by lazy {
        CartRepositoryImpl(apiService)
    }

    private lateinit var context: WeakReference<Context>
    private var debugMode = false
    internal var clientId = ""
    internal var clientSecret = ""

    fun setContext(context: Context) {
        this.context = WeakReference(context.applicationContext)
    }

    fun setDebugMode(debugMode: Boolean) {
        this.debugMode = debugMode
    }

    fun setClientId(clientId: String) {
        this.clientId = clientId
    }

    fun setClientSecret(clientSecret: String) {
        this.clientSecret = clientSecret
    }

    private fun provideRetrofit(): Retrofit {
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
            .addInterceptor(provideAuthorizationInterceptor())
            .addInterceptor(provideLoggingInterceptor()).build()
        return provideRetrofitBuilder()
            .client(client)
            .build()
    }

    private fun provideAuthenticationRetrofit(): Retrofit {
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_IN_SECOND, TimeUnit.SECONDS)
            .addInterceptor(provideLoggingInterceptor())
            .build()
        return provideAuthorizationRetrofitBuilder()
            .client(client)
            .build()
    }

    private fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level =
                if (debugMode) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

    private fun provideAuthorizationInterceptor() =
        AuthorizationInterceptor(authPersistence)

    private fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL)
            .addConverterFactory(
                ConverterFactoryProvider.getMoshiConverterFactory(
                    MoshiBuilderProvider.moshiBuilder.build()
                )
            )
    }

    private fun provideAuthorizationRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.AUTHENTICATION_API_URL)
            .addConverterFactory(
                ConverterFactoryProvider.getMoshiConverterFactory(
                    MoshiBuilderProvider.moshiBuilder.build()
                )
            )
    }

    private inline fun <reified T> buildService(): T {
        return provideRetrofit().create(T::class.java)
    }

    private inline fun <reified T> buildAuthenticationService(): T {
        return provideAuthenticationRetrofit().create(T::class.java)
    }
}
