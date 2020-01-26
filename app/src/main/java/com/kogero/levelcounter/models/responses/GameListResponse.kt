package com.kogero.levelcounter.models.responses

import com.kogero.levelcounter.models.Game

data class GameListResponse(val games: List<Game> = ArrayList())