package com.bob.cryptotracker.core.data.networking

import com.bob.cryptotracker.BuildConfig

fun constructBaseUrl(url: String): String {
    return when {
        url.contains(BuildConfig.BASE_URL) -> url //  /assets -> as example
        url.startsWith("/") -> BuildConfig.BASE_URL + url.drop(1)
        else -> BuildConfig.BASE_URL + url
    }
}