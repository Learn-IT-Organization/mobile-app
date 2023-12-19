package com.example.learnit.ui.feature.register.model

data class RegistrationModel(
    val firstName: String?,
    val lastName: String?,
    val userName: String?,
    val userPassword: String?,
    val gender: String?,
    val userLevel: String?,
    val userPhoto: String?,
    val streak: Int
)