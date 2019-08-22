package com.kogero.levelcounter

import okhttp3.Interceptor
import okhttp3.Response

class LevelCounterInterceptor(private val cacheDuration: Int) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val url = request.url().newBuilder()
            .addQueryParameter("format", "json")
            .build()

        val newRequest = request.newBuilder()
            .url(url)
            .addHeader("Cache-Control", "public, max-age=$cacheDuration")
            .build()

        return chain.proceed(newRequest)
    }
}