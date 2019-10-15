package com.kogero.levelcounter.api

import com.kogero.levelcounter.models.Game
import com.kogero.levelcounter.models.InGameUser
import com.kogero.levelcounter.models.Statistics
import com.kogero.levelcounter.models.UserListViewModel
import com.kogero.levelcounter.models.requests.InGameUserCreationRequest
import com.kogero.levelcounter.models.requests.LoginRequest
import com.kogero.levelcounter.models.requests.SignUpRequest
import com.kogero.levelcounter.models.responses.LoginResponse
import com.kogero.levelcounter.models.responses.SignUpResponse
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

    @POST("game/create")
    fun createGame(): Call<Game>

    @POST("game/addInGameUsers")
    fun addInGameUsers(@Body inGameUserCreationRequest: InGameUserCreationRequest): Call<Game>

    @POST("game/startGame")
    fun startGame(@Query("gameId") gameId: Int): Call<Game>

    @POST("game/updateInGameUser")
    fun updateInGameUser(inGameUser: InGameUser): Call<ResponseBody>

    @GET("game/joinGame")
    fun loadGame(@Query("gameId") gameId: Int): Call<Game>

    @POST("game/quitGame")
    fun quitGame(@Body game: Game): Call<ResponseBody>

    @PUT("game/saveGame")
    fun saveGame(@Body game: Game?): Call<ResponseBody>

    @PUT("game/updateGame")
    fun updateGame(@Body game: Game?): Call<ResponseBody>

    @DELETE("game/delete")
    fun deleteGame(@Query("gameId") gameId: Int): Call<ResponseBody>

    @GET("game/savedGames")
    fun getSavedGames(): Call<List<Game>>

    @GET("game/joinableGames")
    fun getJoinableGames(): Call<List<Game>>

    @GET("game/getPlayers")
    fun getPlayersByGameId(@Query("gameId") gameId: Int): Call<List<InGameUser>>

    @GET("game/joinGame")
    fun joinGame(@Query("gameId") gameId: Int): Call<Game>
}