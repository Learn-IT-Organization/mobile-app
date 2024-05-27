package com.learnitevekri.data.user.teacher.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TeacherRequestData(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String
) : Serializable