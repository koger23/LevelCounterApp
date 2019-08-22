package com.kogero.levelcounter.service

import com.kogero.levelcounter.model.requests.LoginRequest
import com.kogero.levelcounter.model.responses.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LevelCounterApiService {

    @POST("/user/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}