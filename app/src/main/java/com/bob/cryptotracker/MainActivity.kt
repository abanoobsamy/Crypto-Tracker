package com.bob.cryptotracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bob.cryptotracker.core.navigation.AdaptiveCoinListDetailsPanel
import com.bob.cryptotracker.core.presentation.util.ObserveAsEvents
import com.bob.cryptotracker.core.presentation.util.toString
import com.bob.cryptotracker.crypto.presentation.coin_details.CoinDetailsScreen
import com.bob.cryptotracker.crypto.presentation.coin_list.CoinListAction
import com.bob.cryptotracker.crypto.presentation.coin_list.CoinListEvent
import com.bob.cryptotracker.crypto.presentation.coin_list.CoinListState
import com.bob.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import com.bob.cryptotracker.crypto.presentation.coin_list.components.CoinListScreen
import com.bob.cryptotracker.crypto.presentation.coin_list.components.previewCoin
import com.bob.cryptotracker.ui.theme.CryptoTrackerTheme
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTrackerTheme() {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AdaptiveCoinListDetailsPanel(
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@PreviewLightDark
@PreviewDynamicColors
@Composable
fun GreetingPreview() {
    CryptoTrackerTheme() {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            CoinListScreen(
                state = CoinListState(
                    coins = (1..100).map {
                        previewCoin.copy(id = it.toString())
                    }
                ),
                modifier = Modifier
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
                onAction = {}
            )
        }
    }
}