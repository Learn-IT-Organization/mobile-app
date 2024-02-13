package com.example.learnit.data.courses.lessons.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LessonContentData(
    @SerializedName("content_id")
    val contentId: Int,
    @SerializedName("content_type")
    val contentType: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("attachments")
    val attachments: ByteArray,
    @SerializedName("content_lesson_id")
    val contentLessonId: Int
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LessonContentData

        if (contentId != other.contentId) return false
        if (contentType != other.contentType) return false
        if (url != other.url) return false
        if (!attachments.contentEquals(other.attachments)) return false
        if (contentLessonId != other.contentLessonId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contentId
        result = 31 * result + contentType.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + attachments.contentHashCode()
        result = 31 * result + contentLessonId
        return result
    }
}