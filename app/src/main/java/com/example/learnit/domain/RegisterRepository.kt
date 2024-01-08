package com.example.learnit.domain

import com.example.learnit.data.user.register.model.RegistrationData
import com.example.learnit.data.user.register.model.RegistrationResponseData

interface RegisterRepository {
    suspend fun registerUser(registrationData: RegistrationData): RegistrationResponseData?
}