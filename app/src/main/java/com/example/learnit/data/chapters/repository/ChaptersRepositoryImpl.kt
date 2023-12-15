package com.example.learnit.data.chapters.repository

import android.util.Log
import com.example.learnit.domain.chapters.repository.ChaptersRepository
import com.example.learnit.ui.feature.chapters.model.ChapterModel
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.SharedPreferences
import com.example.learnit.data.chapters.mapper.mapToChapterList
import com.example.learnit.data.courses.repository.CourseRepositoryImpl

object ChaptersRepositoryImpl : ChaptersRepository {
    private val apiService = RetrofitAdapter.provideApiService()
    override suspend fun getChapters(): List<ChapterModel> {
        val response = apiService.getChapters()
        Log.d("ChapterResponse1", response.raw().toString())

        if (response.isSuccessful) {
                val responseData = response.body()
                Log.d("ChapterResponse2", response.raw().toString())
                val data = responseData ?: emptyList()
                return data.mapToChapterList()
            }
        Log.d("ChapterResponse3", "EmptyList")
        return emptyList()
    }

    override suspend fun getChaptersByCourseId(courseId: Int): List<ChapterModel> {
        val response = apiService.getChaptersByCourseId(courseId)
        Log.d("ChapterResponse1", response.raw().toString())

        if (response.isSuccessful) {
            val responseData = response.body()
            Log.d("ChapterResponse2", response.raw().toString())
            val data = responseData ?: emptyList()
            return data.mapToChapterList()
        }
        Log.d("ChapterResponse3", "EmptyList")
        return emptyList()
    }
}