package com.bob.cryptotracker.crypto.presentation.coin_list

import com.bob.cryptotracker.core.domain.util.NetworkError

sealed interface CoinListEvent {
    data class Error(val error: NetworkError): CoinListEvent
}