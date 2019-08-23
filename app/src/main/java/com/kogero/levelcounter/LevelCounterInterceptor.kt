package com.kogero.levelcounter

import okhttp3.Interceptor
import okhttp3.Response


class LevelCounterInterceptor : Interceptor {

    var token: String = ""

    fun token(token: String) {
        this.token = token
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (request.header("No-Authentication") == null) {
            //val token = getTokenFromSharedPreference();
            //or use Token Function
            if (token.isNotEmpty()) {
                request = request.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            }
        }
        return chain.proceed(request)
    }
}