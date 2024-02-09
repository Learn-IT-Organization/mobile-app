package com.example.learnit.data.user.login.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import java.io.Serializable

data class LoggedUserData(
    @SerializedName("user_id")
    val user_id: Long,
    @SerializedName("user_role")
    val user_role: String,
    @SerializedName("first_name")
    val first_name: String,
    @SerializedName("last_name")
    val last_name: String,
    @SerializedName("user_name")
    val user_name: String,
    @SerializedName("user_password")
    val user_password: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("user_level")
    val user_level: String,
    @SerializedName("streak")
    val streak: Int
): Serializable