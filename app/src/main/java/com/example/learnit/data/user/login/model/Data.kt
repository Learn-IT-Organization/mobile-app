package com.example.learnit.data.user.login.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("name")
    val name: String,
    @SerialName("value")
    val value: String,
    @SerialName("expires")
    val expires: Long,
)
