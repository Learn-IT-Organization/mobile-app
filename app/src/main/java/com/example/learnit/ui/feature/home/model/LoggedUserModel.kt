package com.example.learnit.ui.feature.home.model

data class LoggedUserModel(
    val userId: Long,
    val userRole: String?,
    val firstName: String?,
    val lastName: String?,
    val userName: String?,
    val userPassword: String?,
    val gender: String?,
    val userLevel: String?,
    val userPhoto: String?,
    val streak: Int?
)