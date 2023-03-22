package co.nimblehq.loyalty.sdk.api.interceptor

import co.nimblehq.loyalty.sdk.persistence.AuthPersistence
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

private const val HEADER_AUTHORIZATION = "Authorization"

class AuthorizationInterceptor constructor(
    private val authPersistence: AuthPersistence
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val createNewRequest = createNewRequest(chain.request())
        val tokenType = authPersistence.getTokenType()
        val accessToken = authPersistence.getAccessToken()
        createNewRequest.addHeader(HEADER_AUTHORIZATION, "$tokenType $accessToken")
        return chain.proceed(createNewRequest.build())
    }

    private fun createNewRequest(request: Request): Request.Builder {
        return request.newBuilder()
    }
}
