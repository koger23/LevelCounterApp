package com.kogero.levelcounter.model.requests

data class LoginRequest(
    val email: String = "user@bloodstone.com",
    val password: String = "Passw0rd",
    val rememberMe: Boolean = false)