package com.learnitevekri.domain.logout

import com.learnitevekri.data.user.logout.LogoutResponseData

interface LogOutRepository {
    suspend fun logOut(): LogoutResponseData
}