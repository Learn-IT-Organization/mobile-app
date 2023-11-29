package com.example.learnit.data.user.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class LoginFormBo(
    @SerialName("id")
    val id: String,
    @SerialName("password")
    val password: String
)