package com.example.learnit.ui.feature.courses.lessons.model

data class LessonContentModel(
    val contentId: Int, 
    val contentType: String,
    val url: String,
    val attachments: ByteArray,
    val contentLessonId: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LessonContentModel

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