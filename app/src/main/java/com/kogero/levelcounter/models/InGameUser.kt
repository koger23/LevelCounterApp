package com.kogero.levelcounter.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class InGameUser (
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
    var Gender: Gender = com.kogero.levelcounter.models.Gender.MALE
) : Serializable
