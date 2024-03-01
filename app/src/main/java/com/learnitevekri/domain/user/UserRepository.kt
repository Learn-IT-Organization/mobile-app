package com.learnitevekri.domain.user

import com.learnitevekri.data.user.login.model.LoggedUserData

interface UserRepository {
    suspend fun getUsers(): List<LoggedUserData>
}