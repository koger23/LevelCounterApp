package com.kogero.levelcounter.model

import com.google.gson.annotations.Expose
import java.sql.Date

data class Game (
    val id: Int,
    val time: Long,
    val dateTime: Date,
    val inGameUsers: List<InGameUser>,
    val hostingUserId: String,
    val isRunning: Boolean = false
)