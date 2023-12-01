package com.example.learnit.data.user.login.mapper

import com.example.learnit.data.user.login.model.LoggedUserData
import com.example.learnit.ui.feature.home.model.UserModel

fun LoggedUserData.mapToUser() = UserModel(
    userId = this.user_id,
    userRole = this.user_role,
    firstName = this.first_name,
    lastName = this.last_name,
    userName = this.user_name,
    userPassword = this.user_password,
    gender = this.gender,
    userLevel = this.user_level
)

fun List<LoggedUserData>.mapToUserList() = map { it.mapToUser() }