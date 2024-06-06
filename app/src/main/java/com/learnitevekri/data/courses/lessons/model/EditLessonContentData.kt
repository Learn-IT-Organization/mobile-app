package com.learnitevekri.data.courses.lessons.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class EditLessonContentData(
    @SerializedName("content_type")
    var contentType: String,
    @SerializedName("url")
    var url: String,
    @SerializedName("content_title")
    var contentTitle: String,
    @SerializedName("content_description")
    var contentDescription: String,
) : Serializable