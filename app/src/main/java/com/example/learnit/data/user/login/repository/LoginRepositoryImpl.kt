package com.example.learnit.data.user.login.repository

import android.util.Log
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.SharedPreferences
import com.example.learnit.data.user.login.model.Data
import com.example.learnit.data.user.login.model.ResponseData
import com.example.learnit.data.user.login.model.LoginData
import com.example.learnit.domain.login.repository.LoginRepository

object LoginRepositoryImpl : LoginRepository {
    override suspend fun getLoginInformation(loginForm: LoginData): ResponseData<Data>? {
        val apiService = RetrofitAdapter.provideApiService()
        val response = apiService.authorizeLogin(loginForm)

        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                val loginData = responseBody.data
                SharedPreferences.storeToken(loginData?.value ?: "")
                val expirationTimeMillis = System.currentTimeMillis() + (loginData?.expires ?: 0)
                SharedPreferences.storeExpires(expirationTimeMillis)
                val loggedUserData = apiService.getLoggedInUser( )
                Log.d("LoginRepositoryImpl", "getLoginInformation: $loggedUserData")
                if (loggedUserData.isSuccessful) {
                    loggedUserData.body()?.let { userData ->
                        val userId = userData.data.user_id
                        SharedPreferences.storeUserId(userId)
                        val userRole = userData.data.user_role
                        SharedPreferences.storeTeacher( userRole == "admin")
                        SharedPreferences.storeTeacher( userRole == "teacher")
                        SharedPreferences.storeTeacher( userRole == "student")
                    }
                }
            }

        }
        return response.body()
    }
}