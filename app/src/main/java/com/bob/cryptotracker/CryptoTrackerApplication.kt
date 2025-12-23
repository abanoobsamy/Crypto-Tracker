package com.bob.cryptotracker

import android.app.Application
import com.bob.cryptotracker.di.appCryptoModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class CryptoTrackerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CryptoTrackerApplication)
            androidLogger()

            modules(appCryptoModule)
        }
    }
}