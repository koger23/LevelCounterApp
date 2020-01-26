package com.kogero.levelcounter.models.requests

data class InGameUserCreationRequest(
    val gameId: Int,
    val userNames: List<String>
)