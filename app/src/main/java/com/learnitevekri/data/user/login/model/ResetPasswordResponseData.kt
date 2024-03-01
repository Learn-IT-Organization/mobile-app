package com.learnitevekri.data.user.login.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResetPasswordResponseData(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String
) : Serializable