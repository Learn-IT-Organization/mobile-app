package com.example.learnit.data.user.login.repository

import android.util.Log
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.SharedPreferences
import com.example.learnit.data.user.login.model.Data
import com.example.learnit.data.user.login.model.LoginData
import com.example.learnit.data.user.login.model.ResponseData
import com.example.learnit.domain.login.LoginRepository

object LoginRepositoryImpl : LoginRepository {
    private val TAG = LoginRepositoryImpl::class.java.simpleName

    override suspend fun getLoginInformation(loginForm: LoginData): ResponseData<Data> {
        val apiService = RetrofitAdapter.provideApiService()
        val response = apiService.authorizeLogin(loginForm)

        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                val loginData = responseBody.data
                SharedPreferences.storeToken(loginData?.value ?: "")
                val expirationTimeMillis = System.currentTimeMillis() + (loginData?.expires ?: 0)
                SharedPreferences.storeExpires(expirationTimeMillis)
                val loggedUserData = apiService.getLoggedInUser()
                Log.d(TAG, "getLoginInformation: $loggedUserData")
                if (loggedUserData.isSuccessful) {
                    loggedUserData.body()?.let { userData ->
                        val userId = userData.data?.userId
                        SharedPreferences.storeUserId(userId)
                        val userRole = userData.data?.userRole
                        SharedPreferences.storeTeacher(userRole == "admin")
                        SharedPreferences.storeTeacher(userRole == "teacher")
                        SharedPreferences.storeTeacher(userRole == "student")
                    }
                }
            }

        }
        return response.body()!!
    }
}