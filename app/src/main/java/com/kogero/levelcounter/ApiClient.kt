package com.kogero.levelcounter

import com.google.gson.GsonBuilder
import com.kogero.levelcounter.helpers.InGameUserDeserializer
import com.kogero.levelcounter.model.InGameUser
import com.kogero.levelcounter.service.ApiInterface
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.kogero.levelcounter.helpers.UnsafeOkHttpClient




object ApiClient {

    var token: String = ""

    fun saveToken(token: String) {
        this.token = token
    }

    var okHttpClient = UnsafeOkHttpClient.unsafeOkHttpClient

    private const val SITE_URL = "https://koger23.myftp.org/"
    private const val BASE_URL = "${SITE_URL}api/"
    val getClient: ApiInterface
        get() {
            val gson = GsonBuilder()
                .registerTypeAdapter(InGameUser::class.java, InGameUserDeserializer())
                .setLenient()
                .create()

            val client = okHttpClient

//            val client = OkHttpClient.Builder()
//                .addInterceptor(LevelCounterInterceptor())
//                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit.create(ApiInterface::class.java)
        }

    class LevelCounterInterceptor : Interceptor {

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
}
