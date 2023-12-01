package com.example.learnit.data.user.login.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//for testing only
@Serializable
data class LoggedUserData(
    @SerialName("user_id")
    val user_id: Int,
    @SerialName("user_role")
    val user_role: String,
    @SerialName("first_name")
    val first_name: String,
    @SerialName("last_name")
    val last_name: String,
    @SerialName("user_name")
    val user_name: String,
    @SerialName("user_password")
    val user_password: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("user_level")
    val user_level: String
)