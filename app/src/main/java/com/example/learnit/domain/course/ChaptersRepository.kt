package com.example.learnit.domain.course

import ChapterResultData
import com.example.learnit.data.courses.chapters.model.ChapterData
import com.example.learnit.data.courses.chapters.model.ChapterWithLessonsData

interface ChaptersRepository {
    suspend fun getChapters(): List<ChapterData>
    suspend fun getChaptersByCourseId(courseId: Int): List<ChapterWithLessonsData>
    suspend fun getChapterResult(courseId: Int, chapterId: Int): ChapterResultData
}