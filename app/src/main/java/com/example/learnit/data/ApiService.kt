package com.example.learnit.data

import com.example.learnit.data.user.login.model.UserData
import retrofit2.http.GET
import retrofit2.Response

interface ApiService {
    @GET("users")
    suspend fun getUsers(): Response<List<UserData>>
}