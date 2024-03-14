package com.learnitevekri.data.user.login.repository

import android.util.Log
import com.learnitevekri.data.RetrofitAdapter
import com.learnitevekri.data.user.login.model.LoggedUserData
import com.learnitevekri.domain.user.UserRepository

object UserRepositoryImpl : UserRepository {
    private val apiService = RetrofitAdapter.provideApiService()
    private val TAG = UserRepositoryImpl::class.java.simpleName

    override suspend fun getUsers(): List<LoggedUserData> {
        try {
            val response = apiService.getUsers()
            if (response.isSuccessful) {
                val responseData = response.body()
                val data = responseData ?: emptyList()
                Log.d(TAG, response.raw().toString())
                return data
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching users: ${e.message}")
        }
        return emptyList()
    }
}