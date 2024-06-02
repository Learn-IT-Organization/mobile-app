package com.learnitevekri.data.courses.chapters.model

import com.google.gson.annotations.SerializedName

data class AddNewChapterResponseData(
    @SerializedName("success")
    val success: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("chapterId")
    val chapterId: Int,
    @SerializedName("userId")
    val userId: Int
) : java.io.Serializable