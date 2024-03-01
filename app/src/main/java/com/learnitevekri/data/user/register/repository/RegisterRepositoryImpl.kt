package com.learnitevekri.data.user.register.repository

import com.learnitevekri.data.RetrofitAdapter
import com.learnitevekri.data.user.register.model.RegistrationData
import com.learnitevekri.data.user.register.model.RegistrationResponseData
import com.learnitevekri.domain.register.RegisterRepository

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