package com.example.learnit.data.user.login.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseUserData(
    @SerialName("success")
    val success: Boolean,
    @SerialName("data")
    val data: LoggedUserData
)
