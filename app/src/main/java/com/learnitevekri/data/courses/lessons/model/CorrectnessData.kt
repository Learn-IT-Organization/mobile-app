package com.learnitevekri.data.courses.lessons.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CorrectnessData(
    @SerializedName("correct")
    val correct: Boolean,
    @SerializedName("responseText")
    val responseText: String
) :Serializable