package com.learnitevekri.data.user.teacher.model

import com.google.gson.annotations.SerializedName

data class TeacherRequestResponseData(
    @SerializedName("success")
    val success: String,
    @SerializedName("message")
    val message: String
) : java.io.Serializable
