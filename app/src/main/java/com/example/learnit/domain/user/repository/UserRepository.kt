package com.example.learnit.domain.user.repository

import com.example.learnit.data.user.login.model.LoggedUserData
import com.example.learnit.ui.feature.home.model.LoggedUserModel

///for testing only
interface UserRepository {
    suspend fun getUsers(): List<LoggedUserData>
}