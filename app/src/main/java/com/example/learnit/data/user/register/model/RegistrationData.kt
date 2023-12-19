package com.example.learnit.data.user.register.model

import kotlinx.serialization.SerialName

data class RegistrationData(
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
    val user_level: String,
    @SerialName("user_photo")
    val user_photo: String,
    @SerialName("streak")
    val streak: Int
)