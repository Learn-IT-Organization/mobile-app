package com.learnitevekri.data.user.register.model

import com.google.gson.annotations.SerializedName

data class RegistrationData(
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
    val streak: Int
) : java.io.Serializable