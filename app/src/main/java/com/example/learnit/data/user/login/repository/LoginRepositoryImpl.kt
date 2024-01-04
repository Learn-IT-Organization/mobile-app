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
                //Csinaljuk egy StringUtils osztaly ahol a stringel kapcsolatos konstansokat taroljuk
                //Peldaul itt ugy hasznalnank hogy StringUtils.EMPTY_STRING
                SharedPreferences.storeToken(loginData?.value ?: "")
                val expirationTimeMillis = System.currentTimeMillis() + (loginData?.expires ?: 0)
                SharedPreferences.storeExpires(expirationTimeMillis)
                val loggedUserData = apiService.getLoggedInUser()
                //Hardcoded TAG
                Log.d("LoginRepositoryImpl", "getLoginInformation: $loggedUserData")
                if (loggedUserData.isSuccessful) {
                    loggedUserData.body()?.let { userData ->
                        //folosleges valtozo (userId)
                        val userId = userData.data?.user_id
                        SharedPreferences.storeUserId(userId)
                        val userRole = userData.data?.user_role
                        //Ha van mar konstans ezekre akkor hasznaljuk is
                        //Nem teljesen ertem ezt a harom sort, haromszor teszitek be a ugyan abba
                        //Szerintem nem kene egy booleant eltarojatok 3 mezore.
                        // Nem lenne egyszerubb ha csak a role-jat taroljatok el egy String-kent?
                        SharedPreferences.storeTeacher(userRole == "admin")
                        SharedPreferences.storeTeacher(userRole == "teacher")
                        SharedPreferences.storeTeacher(userRole == "student")
                    }
                }
            }

        }
        return response.body()
    }
}