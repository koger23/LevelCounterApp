package com.kogero.levelcounter.model.responses

data class SignUpResponse(
    val message: List<String>,
    val UserName: List<String>,
    val Password: List<String>,
    val VerifyPassword: List<String>,
    val Email: String,
    val ErrorMessages: List<String>
)