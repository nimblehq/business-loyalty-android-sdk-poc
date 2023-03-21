package co.nimblehq.loyalty.sdk.network

import co.nimblehq.loyalty.sdk.BuildConfig
import co.nimblehq.loyalty.sdk.api.ApiService
import co.nimblehq.loyalty.sdk.api.AuthenticationService
import co.nimblehq.loyalty.sdk.repository.AuthenticationRepository
import co.nimblehq.loyalty.sdk.repository.AuthenticationRepositoryImpl
import co.nimblehq.loyalty.sdk.repository.RewardRepository
import co.nimblehq.loyalty.sdk.repository.RewardRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

abstract class NetworkBuilder {
    private val apiService: ApiService by lazy { buildService() }
    private val authenticationService: AuthenticationService by lazy { buildAuthenticationService() }

    internal val rewardRepository: RewardRepository by lazy { RewardRepositoryImpl(apiService) }
    internal val authenticationRepository: AuthenticationRepository by lazy {
        AuthenticationRepositoryImpl(authenticationService)
    }

    private var debugMode = false
    private var baseUrl = ""
    private var baseAuthorizationUrl = ""
    private var connectionTimeoutInSecond = 30L
    private var readTimeoutInSecond = 30L
    private var token = ""
    private var tokenType = ""

    fun setDebugMode(debugMode: Boolean): NetworkBuilder {
        this.debugMode = debugMode
        return this
    }

    fun setBaseUrl(url: String): NetworkBuilder {
        this.baseUrl = url
        return this
    }

    fun setBaseAuthorizationUrl(url: String): NetworkBuilder {
        this.baseAuthorizationUrl = url
        return this
    }

    fun setConnectionTimeoutInSecond(timeout: Long): NetworkBuilder {
        this.connectionTimeoutInSecond = timeout
        return this
    }

    fun setReadTimeoutInSecond(timeout: Long): NetworkBuilder {
        this.readTimeoutInSecond = timeout
        return this
    }

    fun setToken(token: String): NetworkBuilder {
        this.token = token
        return this
    }

    fun setTokenType(tokenType: String): NetworkBuilder {
        this.tokenType = tokenType
        return this
    }

    private fun provideRetrofit(): Retrofit {
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(connectionTimeoutInSecond, TimeUnit.SECONDS)
            .readTimeout(readTimeoutInSecond, TimeUnit.SECONDS)
            .addInterceptor(provideLoggingInterceptor()).build()
        return provideRetrofitBuilder()
            .client(client)
            .build()
    }

    private fun provideAuthenticationRetrofit(): Retrofit {
        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(connectionTimeoutInSecond, TimeUnit.SECONDS)
            .readTimeout(readTimeoutInSecond, TimeUnit.SECONDS)
            .addInterceptor(provideLoggingInterceptor()).build() // TODO Add TokenInterceptor
        return provideAuthorizationRetrofitBuilder()
            .client(client)
            .build()
    }

    private fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            level =
                if (debugMode) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

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
