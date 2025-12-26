@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.bob.cryptotracker.core.navigation

import android.widget.Toast
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bob.cryptotracker.core.presentation.util.ObserveAsEvents
import com.bob.cryptotracker.core.presentation.util.toString
import com.bob.cryptotracker.crypto.presentation.coin_details.CoinDetailsScreen
import com.bob.cryptotracker.crypto.presentation.coin_list.CoinListAction
import com.bob.cryptotracker.crypto.presentation.coin_list.CoinListEvent
import com.bob.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import com.bob.cryptotracker.crypto.presentation.coin_list.components.CoinListScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AdaptiveCoinListDetailsPanel(
    modifier: Modifier = Modifier,
    viewModel: CoinListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(events = viewModel.events) { event ->
        when (event) {
            is CoinListEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.toString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    val scope = rememberCoroutineScope()

    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                CoinListScreen(
                    state = state,
                    onAction = {
                        viewModel.onAction(it)
                        when (it) {
                            is CoinListAction.OnCoinClick -> {
                                scope.launch {
                                    navigator.navigateTo(
                                        pane = ListDetailPaneScaffoldRole.Detail
                                    )
                                }
                            }

                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                CoinDetailsScreen(state = state)
            }
        },
        modifier = modifier
    )
}