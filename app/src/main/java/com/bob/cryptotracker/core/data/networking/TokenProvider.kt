package com.bob.cryptotracker.core.data.networking

import com.bob.cryptotracker.BuildConfig

interface TokenProvider {
    fun getToken(): String?
}

class DefaultTokenProvider : TokenProvider {
    override fun getToken(): String = BuildConfig.TOKEN
}
