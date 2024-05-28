package com.learnitevekri.data.user.teacher.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TeacherRequestInfo(
    @SerializedName("request_id")
    val requestId: Int,
    @SerializedName("user_id")
    val userId: Int,
) : Serializable