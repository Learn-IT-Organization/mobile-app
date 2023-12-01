package com.example.learnit.data.user.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class LoginData(
    @SerialName("user_name")
    val user_name: String,
    @SerialName("user_password")
    val user_password: String
)