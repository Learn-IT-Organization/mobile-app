package com.example.learnit.data.user.login.model

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("expires")
    val expires: Long,
) : java.io.Serializable