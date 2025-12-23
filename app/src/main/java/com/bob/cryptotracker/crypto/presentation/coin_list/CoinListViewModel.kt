package com.bob.cryptotracker.crypto.presentation.coin_list

import android.R.attr.action
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bob.cryptotracker.core.domain.util.onError
import com.bob.cryptotracker.core.domain.util.onSuccess
import com.bob.cryptotracker.crypto.domain.CoinDataSource
import com.bob.cryptotracker.crypto.presentation.models.CoinUi
import com.bob.cryptotracker.crypto.presentation.models.toCoinUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class CoinListViewModel(
    private val coinDataSource: CoinDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(CoinListState())
    val state = _state
        .onStart { loadCoins() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            CoinListState()
        )

    private val _events = Channel<CoinListEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: CoinListAction) {
        when (action) {
            is CoinListAction.OnCoinClick -> {
                selectedCoinHistory(coin = action.coin)
            }
        }
    }

    private fun selectedCoinHistory(coin: CoinUi) {
        _state.update { it.copy(selectedCoin = coin) }

        viewModelScope.launch {
            coinDataSource.getCoinHistory(
                coinId = coin.id,
                start = ZonedDateTime.now().minusDays(5),
                end = ZonedDateTime.now(),
            ).onSuccess { history ->
                println(history)
            }.onError { error ->
                _events.send(CoinListEvent.Error(error))
            }
        }
    }

    private fun loadCoins() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            coinDataSource.getCoins()
                .onSuccess { coins ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            coins = coins.map { it.toCoinUi() }
                        )
                    }
                }.onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    _events.send(CoinListEvent.Error(error))
                }
        }
    }
}