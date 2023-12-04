package com.example.learnit.data.user.register.repository

import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.user.register.model.RegistrationData
import com.example.learnit.data.user.register.model.RegistrationResponseData
import com.example.learnit.domain.register.repository.RegisterRepository

object RegisterRepositoryImpl : RegisterRepository {
    override suspend fun registerUser(registrationData: RegistrationData): RegistrationResponseData? {
        val apiService = RetrofitAdapter.provideApiService()
        val response = apiService.registerUser(registrationData)
        if (response.isSuccessful && response.body() != null) {
            return response.body()
        }
        return null
    }
}