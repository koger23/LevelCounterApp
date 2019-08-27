package com.kogero.levelcounter.model;

import com.kogero.levelcounter.model.responses.UserShortResponse

class UserListViewModel(userName: String,
                        statisticsId: Int,
                        avatarUrl: String,
                        val isFriend: Boolean,
                        val isBlocked: Boolean,
                        val relationShipId: Int) :
    UserShortResponse(userName, statisticsId, avatarUrl)
