package com.example.learnit.data.user.login.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class LoggedUserData(
    @SerializedName("user_id")
    val userId: Long,
    @SerializedName("user_role")
    val userRole: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("user_password")
    val userPassword: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("user_level")
    val userLevel: String,
    @SerializedName("streak")
    val streak: Int,
    @SerializedName("last_response_time")
    val lastResponseTime: Date,

    ) : Serializable