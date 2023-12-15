package com.example.learnit.domain.chapters.repository

import com.example.learnit.ui.feature.courses.chapters.model.ChapterModel

interface ChaptersRepository {
    suspend fun getChapters(): List<ChapterModel>
    suspend fun getChaptersByCourseId(courseId: Int): List<ChapterModel>
}