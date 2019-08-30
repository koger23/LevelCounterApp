package com.kogero.levelcounter.model.requests

data class InGameUserCreationRequest(
    val gameId: Int,
    val userNames: List<String>
)