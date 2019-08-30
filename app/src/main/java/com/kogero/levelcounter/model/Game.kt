package com.kogero.levelcounter.model

import java.sql.Date

data class Game (
    val id: Int,
    val time: Long,
    val dateTime: Date,
    val inGameUsers: List<InGameUser>,
    val hostingUserId: String,
    val isRunning: Boolean = false
)