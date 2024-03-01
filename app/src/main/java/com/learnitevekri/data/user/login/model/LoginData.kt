package com.learnitevekri.data.user.login.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginData(
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("user_password")
    val userPassword: String
) : Serializable