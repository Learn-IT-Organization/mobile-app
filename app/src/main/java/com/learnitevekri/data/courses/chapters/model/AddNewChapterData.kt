package com.learnitevekri.data.courses.chapters.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddNewChapterData(
    @SerializedName("chapter_id") var chapterId: Int?,
    @SerializedName("chapter_name") var chapterName: String,
    @SerializedName("chapter_course_id") val chapterCourseId: Int,
    @SerializedName("chapter_description") var chapterDescription: String,
    @SerializedName("chapter_sequence_number") val chapterSequenceNumber: Int,
    @SerializedName("chapter_user_id") val chapterUserId: Int
) : Serializable