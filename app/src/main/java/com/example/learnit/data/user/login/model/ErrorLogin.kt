package com.example.learnit.data.user.login.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorLogin(
    @SerialName("code")
    val code: String,
    @SerialName("msg")
    val message: String
)
