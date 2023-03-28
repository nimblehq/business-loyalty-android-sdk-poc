package co.nimblehq.loyalty.sdk.api

import co.nimblehq.loyalty.sdk.api.response.ErrorResponse
import co.nimblehq.loyalty.sdk.exception.NetworkException
import co.nimblehq.loyalty.sdk.network.MoshiBuilderProvider
import com.squareup.moshi.JsonDataException
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.io.InterruptedIOException
import java.net.UnknownHostException

internal fun Exception.mapError(): Exception {
    return when (this) {
        is UnknownHostException,
        is InterruptedIOException -> NetworkException.NoConnectivity
        is HttpException -> {
            val errorResponse = parseErrorResponse(response())
            NetworkException.ApiResponse(errorResponse?.error ?: message())
        }
        else -> this
    }
}

private fun parseErrorResponse(response: Response<*>?): ErrorResponse? {
    val jsonString = response?.errorBody()?.string()
    return try {
        val moshi = MoshiBuilderProvider.moshiBuilder.build()
        val adapter = moshi.adapter(ErrorResponse::class.java)
        adapter.fromJson(jsonString.orEmpty())
    } catch (exception: IOException) {
        null
    } catch (exception: JsonDataException) {
        null
    }
}