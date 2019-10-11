package com.kogero.levelcounter.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Game (
    var id: Int,
    var time: Long,
    @SerializedName("datetime")
    var dateTime: String,
    @SerializedName("inGameUsers")
    var inGameUsers: List<InGameUser> = ArrayList(),
    var hostingUserId: String,
    var isRunning: Boolean = false
) : Serializable