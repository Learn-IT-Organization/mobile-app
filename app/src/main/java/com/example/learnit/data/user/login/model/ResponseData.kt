package com.example.learnit.data.user.login.model

import kotlinx.serialization.SerialName

data class ResponseData<T> (
    @SerialName("success")
    val success: Boolean,
    @SerialName("data")
    val data: T?,
    @SerialName("err")
    val error: ErrorLogin?,
    @SerialName("servertime")
    val serverTime: Long
)
