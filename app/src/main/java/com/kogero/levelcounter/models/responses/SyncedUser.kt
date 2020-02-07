package com.kogero.levelcounter.models.responses

import com.google.gson.annotations.SerializedName
import com.kogero.levelcounter.models.Gender
import java.io.Serializable

data class SyncedUser (
    @SerializedName("inGameUserId")
    val InGameUserId: Int,
    @SerializedName("userId")
    val UserId: String,
    @SerializedName("userName")
    val UserName: String,
    @SerializedName("level")
    var Level: Int = 1,
    @SerializedName("bonus")
    var Bonus: Int = 0,
    @SerializedName("gameId")
    val GameId: Int,
    @SerializedName("gender")
    var Gender: Gender = com.kogero.levelcounter.models.Gender.MALE,
    @SerializedName("isOnline")
    var IsOnline: Boolean = false,
    @SerializedName("senderId")
    var senderId: String
) : Serializable