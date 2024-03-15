package com.learnitevekri.data.courses.chapters.repository

import ChapterResultData
import android.util.Log
import com.learnitevekri.data.RetrofitAdapter
import com.learnitevekri.data.courses.chapters.model.ChapterWithLessonsData
import com.learnitevekri.domain.course.ChaptersRepository

object ChaptersRepositoryImpl : ChaptersRepository {
    private val apiService = RetrofitAdapter.provideApiService()
    private val TAG: String = ChaptersRepositoryImpl::class.java.simpleName

    override suspend fun getChaptersByCourseId(courseId: Int): List<ChapterWithLessonsData> {
        try {
            val response = apiService.getChaptersByCourseId(courseId)
            Log.d(TAG, response.raw().toString())

            if (response.isSuccessful) {
                val responseData = response.body()
                Log.d(TAG, response.raw().toString())
                return (responseData ?: emptyList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching chapters: ${e.message}")
            throw e
        }
        return emptyList()
    }

    override suspend fun getChapterResult(courseId: Int, chapterId: Int): ChapterResultData {
        val response = apiService.getChapterResult(courseId, chapterId)
        Log.d(TAG, response.raw().toString())

        if (response.isSuccessful) {
            val responseData = response.body()
            Log.d(TAG, response.raw().toString())
            return (responseData ?: ChapterResultData())
        }
        return ChapterResultData()
    }
}