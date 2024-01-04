package com.example.learnit.data.courses.lessons.repository

import android.util.Log
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.courses.lessons.mapper.mapToLessonList
import com.example.learnit.domain.course.repository.LessonRepository
import com.example.learnit.ui.feature.courses.lessons.model.LessonModel

//Ugyan azok a kommentek mint a tobbi Repository-ba
object LessonRepositoryImpl : LessonRepository {
    private val apiService = RetrofitAdapter.provideApiService()

    override suspend fun getLessons(): List<LessonModel> {
        try {
            val response = apiService.getLessons()
            if (response.isSuccessful) {
                val responseData = response.body()
                val data = responseData ?: emptyList()
                return data.mapToLessonList()
            }
        } catch (e: Exception) {
            Log.e("LessonRepository", "Error fetching lessons: ${e.message}")
        }
        return emptyList()
    }

    override suspend fun getLessonsByChapterId(chapterId: Int): List<LessonModel> {
        try {
            val response = apiService.getLessonsByChapterId(chapterId)
            if (response.isSuccessful) {
                val responseData = response.body()
                val data = responseData ?: emptyList()
                return data.mapToLessonList()
            }
        } catch (e: Exception) {
            Log.e("LessonRepository", "Error fetching lessons by chapter id: ${e.message}")
        }
        return emptyList()
    }
}