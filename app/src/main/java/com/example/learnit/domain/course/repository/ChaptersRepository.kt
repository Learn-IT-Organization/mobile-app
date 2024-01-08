package com.example.learnit.domain.course.repository

import com.example.learnit.ui.feature.courses.chapters.model.ChapterModel

interface ChaptersRepository {
    suspend fun getChapters(): List<ChapterModel>
    suspend fun getChaptersByCourseId(courseId: Int): List<ChapterModel>
}