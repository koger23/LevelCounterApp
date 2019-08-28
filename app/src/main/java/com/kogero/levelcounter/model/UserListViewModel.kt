package com.kogero.levelcounter.model;

class UserListViewModel(
    val userName: String,
    val statisticsId: Int,
    val avatarUrl: String,
    val isFriend: Boolean,
    val isBlocked: Boolean,
    val isPending: Boolean,
    val relationShipId: Int
)