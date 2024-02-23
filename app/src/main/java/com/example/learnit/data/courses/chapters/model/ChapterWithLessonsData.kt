package com.example.learnit.data.courses.chapters.model

import ChapterResultData
import com.example.learnit.data.courses.lessons.model.LessonData
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChapterWithLessonsData(
    @SerializedName("chapter") val chapter: ChapterData,
    @SerializedName("lessons") val lessons: List<LessonData>,
    var chapterResultData: ChapterResultData
) : Serializable