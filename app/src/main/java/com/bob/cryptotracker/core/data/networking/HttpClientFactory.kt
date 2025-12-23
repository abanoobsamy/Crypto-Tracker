package com.bob.cryptotracker.core.data.networking

import android.os.AsyncTask.execute
import com.bob.cryptotracker.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(
        engine: HttpClientEngine,
        tokenProvider: TokenProvider
    ): HttpClient {
        return HttpClient(engine) {
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.ANDROID

//                sanitizeHeader { it == HttpHeaders.Authorization } // if need to hide token
            }
            install(ContentNegotiation) {
                json(
                    json = Json {
                        prettyPrint = true // make json more readable at logcat
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(AuthPlugin)

            defaultRequest {
                contentType(ContentType.Application.Json)
            }

//            defaultRequest {
//                header(
//                    HttpHeaders.Authorization,
//                    "Bearer ${BuildConfig.TOKEN}"
//                )
//                accept(ContentType.Application.Json)
//            }
        }
    }

    val AuthPlugin = createClientPlugin("AuthPlugin") {

        onRequest { request, _ ->
            val needsAuth =
                request.attributes.getOrNull(RequiresAuth) == true

            if (needsAuth) {
                val token = BuildConfig.TOKEN
                request.headers.append(
                    HttpHeaders.Authorization,
                    "Bearer $token"
                )
            }
        }
    }

}