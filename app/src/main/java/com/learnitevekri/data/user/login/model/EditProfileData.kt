package com.learnitevekri.data.user.login.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class EditProfileData(
    @SerializedName("first_name")
    val firstName: String?,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("user_name")
    val userName: String?,
    @SerializedName("user_password")
    val userPassword: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("user_level")
    val userLevel: String?,
)
