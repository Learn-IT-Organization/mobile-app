package com.learnitevekri.data.user.teacher.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TeacherRequestDataFull(

    @SerializedName("request_id")
    val requestId: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("is_approved")
    val isApproved: String,

    @SerializedName("full_name")
    val fullName: String

) : Serializable