package com.kogero.levelcounter.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.sql.Date

data class Game (
    var id: Int,
    var time: Long,
    var dateTime: Date = Date(1),
    @SerializedName("inGameUsers")
    var inGameUsers: List<InGameUser> = ArrayList(),
    var hostingUserId: String,
    var isRunning: Boolean = false
) : Serializable