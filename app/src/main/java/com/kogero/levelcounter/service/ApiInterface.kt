package com.kogero.levelcounter.service

import com.kogero.levelcounter.model.Statistics
import com.kogero.levelcounter.model.UserListViewModel
import com.kogero.levelcounter.model.requests.LoginRequest
import com.kogero.levelcounter.model.requests.SignUpRequest
import com.kogero.levelcounter.model.responses.LoginResponse
import com.kogero.levelcounter.model.responses.SignUpResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST("user/login")
    @Headers("No-Authentication: true", "User-Agent: LevelCounterApp")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("user/signup")
    @Headers("No-Authentication: true", "User-Agent: LevelCounterApp")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    @GET("user/friends")
    fun getFriends(): Call<List<UserListViewModel>>

    @GET("statistics/{id}")
    fun getStatisticsById(@Path("id") id: Int): Call<Statistics>

    @GET("user/userlist")
    fun getAllUsers(): Call<List<UserListViewModel>>

    @POST("relationship/connect")
    fun makeRequest(@Query("userName") userName: String): Call<ResponseBody>

    @POST("relationship/block")
    fun blockUser(@Query("userName") userName: String): Call<ResponseBody>

    @GET("user/requests/pending")
    fun getPendingRequests(): Call<List<UserListViewModel>>

    @PUT("user/requests/{relationshipId}/confirm")
    fun confirmRequest(@Path("relationshipId") relationshipId: Int): Call<ResponseBody>

    @PUT("user/requests/{relationshipId}/dismiss")
    fun dismissRequest(@Path("relationshipId") relationshipId: Int): Call<ResponseBody>

    @GET("statistics/userstats")
    fun getPersonalStats(): Call<Statistics>
}