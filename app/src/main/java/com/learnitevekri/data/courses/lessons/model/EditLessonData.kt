package com.learnitevekri.data.courses.lessons.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EditLessonData(
    @SerializedName("lesson_name") var lessonName: String?,
    @SerializedName("lesson_description") var lessonDescription: String?,
    @SerializedName("lesson_type") var lessonType: String?,
    @SerializedName("lesson_tags") var lessonTags: String?,
) : Serializable

