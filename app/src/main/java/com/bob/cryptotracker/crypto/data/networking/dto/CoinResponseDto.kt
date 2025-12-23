package com.bob.cryptotracker.crypto.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinResponseDto(
    val data: List<CoinDto>,
    val timestamp: Long
)
