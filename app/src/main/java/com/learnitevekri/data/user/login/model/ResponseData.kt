package com.learnitevekri.data.user.login.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ResponseData<T>(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: T?,
    @SerializedName("err")
    val error: ErrorLogin,
    @SerializedName("servertime")
    val serverTime: Long
) : Serializable