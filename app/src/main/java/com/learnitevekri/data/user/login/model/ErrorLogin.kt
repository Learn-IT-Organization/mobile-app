package com.learnitevekri.data.user.login.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ErrorLogin(
    @SerializedName("code")
    val code: String,
    @SerializedName("msg")
    val message: String
) : Serializable