package com.example.learnit.data.user.login.repository

import android.util.Log
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.user.login.mapper.mapToUserList
import com.example.learnit.domain.user.repository.UserRepository
import com.example.learnit.ui.feature.home.model.LoggedUserModel

//for testing only
object UserRepositoryImpl : UserRepository {
    private val apiService = RetrofitAdapter.provideApiService()

    override suspend fun getUsers(): List<LoggedUserModel> {
        try {
            val response = apiService.getUsers()
            if (response.isSuccessful) {
                val responseData = response.body()
                val data = responseData ?: emptyList()
                Log.d("Response", response.raw().toString())
                return data.mapToUserList()
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error fetching users: ${e.message}")
        }
        return emptyList()
    }

}