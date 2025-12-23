package com.bob.cryptotracker.core.data.networking

import com.bob.cryptotracker.core.domain.util.NetworkError
import com.bob.cryptotracker.core.domain.util.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.util.AttributeKey

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T, NetworkError> {
    return when(response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                Result.Error(NetworkError.SERIALIZATION)
            } catch (e: Exception) {
                Result.Error(NetworkError.UNKNOWN)
            }
        }
        408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
        429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
        else -> Result.Error(NetworkError.UNKNOWN)
    }
}

val RequiresAuth = AttributeKey<Boolean>("RequiresAuth")

fun HttpRequestBuilder.requiresAuth() {
    attributes.put(RequiresAuth, true)
}
