package com.learnitevekri.data.user.logout

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LogoutResponseData(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("message")
    val message: String = ""
) : Serializable