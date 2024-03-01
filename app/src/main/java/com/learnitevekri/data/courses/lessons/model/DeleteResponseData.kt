package com.learnitevekri.data.courses.lessons.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DeleteResponseData(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String
) : Serializable