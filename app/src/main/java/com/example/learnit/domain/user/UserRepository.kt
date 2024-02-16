package com.example.learnit.domain.user

import com.example.learnit.data.user.login.model.LoggedUserData

interface UserRepository {
    suspend fun getUsers(): List<LoggedUserData>
}