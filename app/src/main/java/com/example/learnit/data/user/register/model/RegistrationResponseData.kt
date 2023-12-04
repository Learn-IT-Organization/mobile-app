package com.example.learnit.data.user.register.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegistrationResponseData(
    @SerialName("success")
    val success: String,
    @SerialName("message")
    val message: String
)