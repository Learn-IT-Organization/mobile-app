package com.example.learnit.data.user.login.mapper

import com.example.learnit.data.user.login.model.LoggedUserData
import com.example.learnit.data.user.login.model.LoggedUserResponseData
import com.example.learnit.data.user.login.model.ResponseData
import com.example.learnit.ui.feature.home.model.LoggedUserModel
import com.example.learnit.ui.feature.home.model.LoggedUserResponseModel

fun LoggedUserData.mapToUser() = LoggedUserModel(
    userId = this.user_id,
    userRole = this.user_role,
    firstName = this.first_name,
    lastName = this.last_name,
    userName = this.user_name,
    userPassword = this.user_password,
    gender = this.gender,
    userLevel = this.user_level,
    userPhoto = this.user_photo,
    streak = this.streak
)

fun List<LoggedUserData>.mapToUserList() = map { it.mapToUser() }

fun LoggedUserResponseData.mapToUserResponseModel() = LoggedUserResponseModel(
    success = this.success,
    data = this.data,
    err = this.error,
    message = this.message,
    servertime = this.serverTime
)