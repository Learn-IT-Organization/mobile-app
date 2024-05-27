package com.learnitevekri.data.user.teacher.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Message(
    @SerializedName("message")
    val message: String,
) : Serializable