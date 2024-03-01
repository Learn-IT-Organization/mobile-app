package com.learnitevekri.domain.register

import com.learnitevekri.data.user.register.model.RegistrationData
import com.learnitevekri.data.user.register.model.RegistrationResponseData

interface RegisterRepository {
    suspend fun registerUser(registrationData: RegistrationData): RegistrationResponseData?
}