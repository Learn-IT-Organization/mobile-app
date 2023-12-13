package com.example.learnit.domain.chapters.repository

import com.example.learnit.ui.feature.chapters.model.ChapterModel

interface ChaptersRepository {
    suspend fun getChapters(): List<ChapterModel>

}