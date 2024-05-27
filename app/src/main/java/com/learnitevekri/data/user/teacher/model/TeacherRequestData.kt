package com.learnitevekri.data.user.teacher.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date
data class TeacherRequestData(
    @SerializedName("request_id")
    val requestId: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("request_time")
    val requestTime: Date,
    @SerializedName("is_approved")
    val isApproved: String,
) : Serializable