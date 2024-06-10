package com.learnitevekri.data.courses.chapters.repository

import ChapterResultData
import android.util.Log
import com.learnitevekri.data.RetrofitAdapter
import com.learnitevekri.data.courses.chapters.model.AddNewChapterData
import com.learnitevekri.data.courses.chapters.model.AddNewChapterResponseData
import com.learnitevekri.data.courses.chapters.model.ChapterData
import com.learnitevekri.data.courses.chapters.model.ChapterWithLessonsData
import com.learnitevekri.data.courses.chapters.model.EditChapterData
import com.learnitevekri.data.courses.course.repository.CourseRepositoryImpl
import com.learnitevekri.data.courses.lessons.model.DeleteResponseData
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
                return (responseData ?: emptyList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching chapters: ${e.message}")
            throw e
        }
        return emptyList()
    }

    override suspend fun getChapterResult(courseId: Int, chapterId: Int): ChapterResultData {
        try {
            val response = apiService.getChapterResult(courseId, chapterId)
            Log.d(TAG, response.raw().toString())

            if (response.isSuccessful) {
                val responseData = response.body()
                Log.d(TAG, response.raw().toString())
                return (responseData ?: ChapterResultData())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching chapter result: ${e.message}")
            throw e
        }
        return ChapterResultData()
    }

    override suspend fun addNewChapter(addNewChapterData: AddNewChapterData): Int? {
        try {
            val response = apiService.addNewChapter(addNewChapterData)
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, response.body()?.chapterId.toString())
                return response.body()?.chapterId
            }
        } catch (e: Exception) {
            Log.e(CourseRepositoryImpl.TAG, "Error adding new chapter: ${e.message}")
            throw e
        }
        return null
    }

    override suspend fun editChapter(
        chapterId: Int,
        editChapterData: EditChapterData
    ): AddNewChapterResponseData {
        try {
            val response = apiService.editChapter(chapterId, editChapterData)
            Log.d(TAG, "Attempt to update chapter with ID $chapterId")

            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "Chapter updated successfully: ${response.body()}")
                return response.body()!!
            } else {
                Log.e(TAG, "Failed to update chapter: ${response.errorBody()?.string()}")
                throw RuntimeException("Failed to update chapter due to server error")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating chapter: ${e.message}")
            throw e
        }
    }

    override suspend fun getChapterById(chapterId: Int): ChapterData {
        try {
            val response = apiService.getChapterById(chapterId)
            if (response.isSuccessful) {
                return response.body()!!
            } else {
                Log.e(TAG, "Error fetching chapter by ID: ${response.code()}")
                throw RuntimeException("Failed to fetch chapter by ID")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching chapter by ID: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteChapter(chapterId: Int): DeleteResponseData {
        try {
            val response = apiService.deleteChapter(chapterId)
            if (response.isSuccessful) {
                return response.body()!!
            } else {
                Log.e(TAG, "Error deleting chapter: ${response.code()}")
                throw RuntimeException("Failed to delete chapter")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting chapter: ${e.message}")
            throw e
        }
    }
}