package com.learnitevekri.domain.course

import ChapterResultData
import com.learnitevekri.data.courses.chapters.model.ChapterWithLessonsData

interface ChaptersRepository {
    suspend fun getChaptersByCourseId(courseId: Int): List<ChapterWithLessonsData>
    suspend fun getChapterResult(courseId: Int, chapterId: Int): ChapterResultData
}