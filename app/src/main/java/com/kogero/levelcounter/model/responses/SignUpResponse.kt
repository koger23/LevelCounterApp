package com.kogero.levelcounter.model.responses

class SignUpResponse(
    val message: List<String>,
    val UserName: List<String>,
    val Password: List<String>,
    val VerifyPassword: List<String>,
    val Email: List<String>
)