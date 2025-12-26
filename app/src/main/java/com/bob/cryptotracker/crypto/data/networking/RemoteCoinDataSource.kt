package com.bob.cryptotracker.crypto.data.networking

import com.bob.cryptotracker.core.data.networking.constructBaseUrl
import com.bob.cryptotracker.core.data.networking.requiresAuth
import com.bob.cryptotracker.core.data.networking.safeCall
import com.bob.cryptotracker.core.domain.util.NetworkError
import com.bob.cryptotracker.core.domain.util.Result
import com.bob.cryptotracker.core.domain.util.map
import com.bob.cryptotracker.crypto.data.mappers.toCoin
import com.bob.cryptotracker.crypto.data.mappers.toCoinPrice
import com.bob.cryptotracker.crypto.data.networking.dto.CoinHistoryDto
import com.bob.cryptotracker.crypto.data.networking.dto.CoinResponseDto
import com.bob.cryptotracker.crypto.domain.Coin
import com.bob.cryptotracker.crypto.domain.CoinDataSource
import com.bob.cryptotracker.crypto.domain.CoinPrice
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.ZoneId
import java.time.ZonedDateTime

class RemoteCoinDataSource(
    private val httpClient: HttpClient
) : CoinDataSource {

    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        return safeCall<CoinResponseDto> {
            httpClient.get(
                urlString = constructBaseUrl("assets")
            ) {
                requiresAuth()
            }.body()
        }.map { response ->
            response.data.map { it.toCoin() }
        }
    }

    override suspend fun getCoinHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError> {
        val startMillis = start
            .withZoneSameInstant(ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()
        val endMillis = end
            .withZoneSameInstant(ZoneId.of("UTC"))
            .toInstant()
            .toEpochMilli()

        return safeCall<CoinHistoryDto> {
            httpClient.get(
                urlString = constructBaseUrl("assets/$coinId/history")
            ) {
                requiresAuth()
                parameter("interval", "h6")
                parameter("start", startMillis)
                parameter("end", endMillis)
            }.body()
        }.map { response ->
            response.data.map { it.toCoinPrice() }
        }
    }
}