package com.example.learnit.data.courses.lessons.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LessonContentData(
    @SerialName("content_id")
    val content_id: Int,

    @SerialName("content_type")
    val content_type: String,

    @SerialName("url")
    val url: String,

    @SerialName("attachments")
    val attachments: ByteArray,

    @SerialName("content_lesson_id")
    val content_lesson_id: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LessonContentData

        if (content_id != other.content_id) return false
        if (content_type != other.content_type) return false
        if (url != other.url) return false
        if (!attachments.contentEquals(other.attachments)) return false
        if (content_lesson_id != other.content_lesson_id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content_id
        result = 31 * result + content_type.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + attachments.contentHashCode()
        result = 31 * result + content_lesson_id
        return result
    }
}