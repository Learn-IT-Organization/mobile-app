package com.learnitevekri.data.courses.chapters.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EditChapterData(
    @SerializedName("chapter_name") var chapterName: String?,
    @SerializedName("chapter_description") var chapterDescription: String?,
) : Serializable