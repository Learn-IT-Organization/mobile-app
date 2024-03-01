package com.learnitevekri.data.user.register.model

import com.google.gson.annotations.SerializedName

data class RegistrationResponseData(
    @SerializedName("success")
    val success: String,
    @SerializedName("message")
    val message: String
) : java.io.Serializable