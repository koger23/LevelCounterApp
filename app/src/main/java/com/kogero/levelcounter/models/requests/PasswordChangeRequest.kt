package com.kogero.levelcounter.models.requests

data class PasswordChangeRequest(
    val currentPassword: String,
    val newPassword: String,
    val verifyPassword: String
)