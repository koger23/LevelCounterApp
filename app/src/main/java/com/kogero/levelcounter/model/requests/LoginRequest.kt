package com.kogero.levelcounter.model.requests

data class LoginRequest(
    val email: String,
    val password: String)