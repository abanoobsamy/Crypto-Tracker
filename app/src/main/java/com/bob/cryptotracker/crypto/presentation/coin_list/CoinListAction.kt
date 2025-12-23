package com.bob.cryptotracker.crypto.presentation.coin_list

import com.bob.cryptotracker.crypto.presentation.models.CoinUi

sealed interface CoinListAction {
    data class OnCoinClick(val coin: CoinUi) : CoinListAction
}