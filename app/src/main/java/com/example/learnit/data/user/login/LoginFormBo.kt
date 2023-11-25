package com.example.learnit.data.user.login

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class LoginFormBo(
    @SerialName("id")
    val id: String,
    @SerialName("password")
    val password: String
)