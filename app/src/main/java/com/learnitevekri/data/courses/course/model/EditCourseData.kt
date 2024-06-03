package com.learnitevekri.data.courses.course.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EditCourseData(
    @SerializedName("course_name") var courseName: String?,
    @SerializedName("programming_language") var programmingLanguage: String?,
    @SerializedName("course_description") var courseDescription: String?,
) : Serializable
