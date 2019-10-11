package com.kogero.levelcounter.models.requests

data class LoginRequest(
    val userName: String,
    val password: String)