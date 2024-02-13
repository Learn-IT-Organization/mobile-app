package com.example.learnit.data.courses.chapters.repository

import android.util.Log
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.courses.chapters.model.ChapterData
import com.example.learnit.data.courses.chapters.model.ChapterWithLessonsData
import com.example.learnit.domain.course.repository.ChaptersRepository

object ChaptersRepositoryImpl : ChaptersRepository {
    private val apiService = RetrofitAdapter.provideApiService()
    private val TAG: String = ChaptersRepositoryImpl::class.java.simpleName

    override suspend fun getChapters(): List<ChapterData> {
        val response = apiService.getChapters()
        Log.d(TAG, response.raw().toString())

        if (response.isSuccessful) {
            val responseData = response.body()
            Log.d(TAG, response.raw().toString())
            return (responseData ?: emptyList())
        }
        return emptyList()
    }

    override suspend fun getChaptersByCourseId(courseId: Int): List<ChapterWithLessonsData> {
        val response = apiService.getChaptersByCourseId(courseId)
        Log.d(TAG, response.raw().toString())

        if (response.isSuccessful) {
            val responseData = response.body()
            Log.d(TAG, response.raw().toString())
            return (responseData ?: emptyList())
        }
        return emptyList()
    }
}