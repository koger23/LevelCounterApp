package com.kogero.levelcounter.models.responses

data class UserResponse (
    val userName: String,
    val fullName: String,
    val sex: String,
    val email: String,
    val registerDate: String,
    val avatarUrl: String
)