package com.kogero.levelcounter.models.requests

data class UserEditRequest(
    val fullName: String,
    val userName: String,
    val email: String,
    val avatarUrl: String = "",
    val currentPassword: String,
    val newPassword: String? = null,
    val verifyNewPassword: String? = null
)