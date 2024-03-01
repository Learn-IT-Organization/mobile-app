package com.learnitevekri.data.user.login.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ForgotPasswordData(
    @SerializedName("userName")
    val userName: String,
    @SerializedName("userEmail")
    val userEmail: String
) : Serializable