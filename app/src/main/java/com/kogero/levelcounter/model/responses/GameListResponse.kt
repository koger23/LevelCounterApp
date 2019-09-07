package com.kogero.levelcounter.model.responses

import com.kogero.levelcounter.model.Game

data class GameListResponse(val games: List<Game> = ArrayList())