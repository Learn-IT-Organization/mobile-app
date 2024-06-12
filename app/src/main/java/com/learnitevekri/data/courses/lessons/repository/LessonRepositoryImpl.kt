package com.learnitevekri.data.courses.lessons.repository

import android.util.Log
import com.learnitevekri.data.RetrofitAdapter
import com.learnitevekri.data.courses.lessons.model.AddLessonContentResponseData
import com.learnitevekri.data.courses.lessons.model.AddNewLessonData
import com.learnitevekri.data.courses.lessons.model.AddNewLessonResponseData
import com.learnitevekri.data.courses.lessons.model.DeleteContentResponse
import com.learnitevekri.data.courses.lessons.model.DeleteResponseData
import com.learnitevekri.data.courses.lessons.model.EditLessonContentData
import com.learnitevekri.data.courses.lessons.model.EditLessonData
import com.learnitevekri.data.courses.lessons.model.LessonContentData
import com.learnitevekri.data.courses.lessons.model.LessonData
import com.learnitevekri.data.courses.lessons.model.LessonProgressData
import com.learnitevekri.data.courses.lessons.model.UserAnswersData
import com.learnitevekri.domain.course.LessonRepository

object LessonRepositoryImpl : LessonRepository {

    val TAG: String = LessonRepositoryImpl::class.java.simpleName
    private val apiService = RetrofitAdapter.provideApiService()

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

    override suspend fun getLessonContentByLessonId(
        lessonId: Int
    ): List<LessonContentData> {
        try {
            val response = apiService.getLessonContentByLessonId(lessonId)
            if (response.isSuccessful) {
                return response.body() ?: emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching lesson result: ${e.message}")
            throw e
        }
        return emptyList()
    }

    override suspend fun getLessonProgress(): List<LessonProgressData> {
        try {
            val response = apiService.getLessonProgress()
            if (response.isSuccessful) {
                Log.d(TAG, "Lesson progress response: ${response.body()}")
                return response.body() ?: emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching lesson progress: ${e.message}")
            throw e
        }
        return emptyList()
    }

    override suspend fun getLessonById(lessonId: Int): LessonData {
        try {
            val response = apiService.getLessonById(lessonId)
            if (response.isSuccessful) {
                return response.body()!!
            } else {
                Log.e(TAG, "Error fetching lesson by ID: ${response.code()}")
                throw RuntimeException("Failed to fetch lesson by ID")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching lesson by ID: ${e.message}")
            throw e
        }
    }

    override suspend fun getLessonResultWithValidation(lessonId: Int): List<UserAnswersData> {
        try {
            val response = apiService.getLessonResultWithValidation(lessonId)
            if (response.isSuccessful) {
                return response.body() ?: emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching lesson result with validation: ${e.message}")
            throw e
        }
        return emptyList()
    }

    override suspend fun addNewLesson(addNewLessonData: AddNewLessonData): Int? {
        try {
            val response = apiService.addNewLesson(addNewLessonData)
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, response.body()?.lessonId.toString())
                return response.body()?.lessonId
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error adding new lesson: ${e.message}")
            throw e
        }
        return null
    }

    override suspend fun editLesson(
        lessonId: Int,
        editLessonData: EditLessonData
    ): AddNewLessonResponseData {
        try {
            val response = apiService.editLesson(lessonId, editLessonData)
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "Lesson updated successfully: ${response.body()}")
                return response.body()!!
            } else {
                Log.e(TAG, "Failed to update lesson: ${response.errorBody()?.string()}")
                throw RuntimeException("Failed to update lesson due to server error")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating lesson: ${e.message}")
            throw e
        }
    }

    override suspend fun createLessonContent(lessonContentData: LessonContentData): Int? {
        try {
            val response = apiService.createLessonContent(lessonContentData)
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "Lesson content id: ${response.body()?.contentId.toString()}")
                return response.body()?.contentId
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating lesson content: ${e.message}")
            throw e
        }
        return null
    }

    override suspend fun editLessonContent(
        lessonContentId: Int,
        editLessonContentData: EditLessonContentData
    ): AddLessonContentResponseData {
        try {
            val response = apiService.editLessonContent(lessonContentId, editLessonContentData)
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "Lesson content updated successfully: ${response.body()}")
                return response.body()!!
            } else {
                Log.e(TAG, "Failed to update lesson content: ${response.errorBody()?.string()}")
                throw RuntimeException("Failed to update lesson content due to server error")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating lesson: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteLesson(lessonId: Int): DeleteResponseData {
        try {
            val response = apiService.deleteLesson(lessonId)
            if (response.isSuccessful) {
                return response.body()!!
            } else {
                Log.e(TAG, "Error deleting lesson: ${response.code()}")
                throw RuntimeException("Failed to delete lesson")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting lesson: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteLessonContent(lessonContentId: Int): DeleteContentResponse {
        try {
            val response = apiService.deleteLessonContent(lessonContentId)
            if (response.isSuccessful) {
                return response.body()!!
            } else {
                Log.e(TAG, "Error deleting lesson content: ${response.code()}")
                throw RuntimeException("Failed to delete lesson content")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting lesson content: ${e.message}")
            throw e
        }
    }
}