package com.example.learnit.data.courses.lessons.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CorrectnessData(
    @SerializedName("correctness")
    val correctness: Boolean,
    @SerializedName("responseText")
    val responseText: String
) :Serializable