package com.limestudio.findlottery.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    private var headerToken = ""

    fun setTokenHeader(token: String) {
        this.headerToken = token
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder().header(
            "Authorization",
            "Bearer $headerToken"
        )
        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }
}