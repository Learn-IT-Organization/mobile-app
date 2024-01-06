package com.example.learnit.data.courses.quiz.model

import kotlinx.serialization.SerialName

data class UserResponseData (
    @SerialName("success")
    val success: String,
    @SerialName("message")
    val message: String
)