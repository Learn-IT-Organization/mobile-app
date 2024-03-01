package com.learnitevekri.data.user.login.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResetCodeData(
    @SerializedName("userName")
    val userName: String,
    @SerializedName("resetCode")
    val resetCode: Long,
    @SerializedName("newPassword")
    val newPassword: String
) : Serializable