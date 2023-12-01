package com.example.learnit.domain.user.repository

import com.example.learnit.ui.feature.home.model.UserModel

///for testing only
interface UserRepository {
    suspend fun getUsers(): List<UserModel>
}