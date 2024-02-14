package com.example.learnit.data.courses.lessons.repository

import android.util.Log
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.courses.lessons.model.LessonData
import com.example.learnit.data.courses.lessons.model.LessonResultData
import com.example.learnit.domain.course.repository.LessonRepository

object LessonRepositoryImpl : LessonRepository {

    val TAG: String = LessonRepositoryImpl::class.java.simpleName
    private val apiService = RetrofitAdapter.provideApiService()

    override suspend fun getLessons(): List<LessonData> {
        try {
            val response = apiService.getLessons()
            if (response.isSuccessful) {
                val responseData = response.body()
                return responseData ?: emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching lessons: ${e.message}")
        }
        return emptyList()
    }

    override suspend fun getLessonsByChapterId(chapterId: Int): List<LessonData> {
        try {
            val response = apiService.getLessonsByChapterId(chapterId)
            if (response.isSuccessful) {
                val responseData = response.body()
                return responseData ?: emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching lessons by chapter id: ${e.message}")
        }
        return emptyList()
    }

    override suspend fun getLessonResult(lessonId: Int): LessonResultData {
        try {
            val response = apiService.getLessonResult(lessonId)
            if (response.isSuccessful) {
                return response.body() ?: LessonResultData()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching lesson result: ${e.message}")
        }
        return LessonResultData()
    }

}