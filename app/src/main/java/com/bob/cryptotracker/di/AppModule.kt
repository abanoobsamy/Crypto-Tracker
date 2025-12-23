package com.bob.cryptotracker.di

import com.bob.cryptotracker.core.data.networking.DefaultTokenProvider
import com.bob.cryptotracker.core.data.networking.HttpClientFactory
import com.bob.cryptotracker.core.data.networking.TokenProvider
import com.bob.cryptotracker.crypto.data.networking.RemoteCoinDataSource
import com.bob.cryptotracker.crypto.domain.CoinDataSource
import com.bob.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appCryptoModule = module {
    single<TokenProvider> {
        DefaultTokenProvider()
    }
    single {
        HttpClientFactory.create(
            CIO.create(),
            tokenProvider = get()
        )
    }
    singleOf(::RemoteCoinDataSource).bind<CoinDataSource>()

    viewModelOf(::CoinListViewModel)
}