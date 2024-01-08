package com.example.learnit.data.courses.lessons.repository

import android.util.Log
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.courses.lessons.mapper.mapToLessonList
import com.example.learnit.data.courses.quiz.mapper.mapToMultipleChoiceQAList
import com.example.learnit.data.courses.quiz.model.MultipleChoiceResponseData
import com.example.learnit.domain.course.repository.LessonRepository
import com.example.learnit.ui.feature.courses.lessons.model.LessonModel
import com.example.learnit.ui.feature.courses.quiz.model.MultipleChoiceQuestionAnswerModel

object LessonRepositoryImpl : LessonRepository {

    val TAG: String = LessonRepositoryImpl::class.java.simpleName

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
            Log.e(TAG, "Error fetching lessons: ${e.message}")
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
            Log.e(TAG, "Error fetching lessons by chapter id: ${e.message}")
        }
        return emptyList()
    }

    override suspend fun getQuestionsAnswersByCourseIdChapterIdLessonIdMultipleChoice(
        courseId: Int,
        chapterId: Int,
        lessonId: Int
    ): List<MultipleChoiceQuestionAnswerModel> {
        try {
            val response = apiService.getQuestionsAnswersByCourseIdChapterIdLessonIdMultipleChoice(
                courseId,
                chapterId,
                lessonId
            )
            if (response.isSuccessful) {
                val responseData = response.body()
                val data = responseData ?: emptyList()
                return data.mapToMultipleChoiceQAList()
            }
        } catch (e: Exception) {
            Log.e(
                TAG,
                "Error fetching questions and answers by course id, chapter id and lesson id: ${e.message}"
            )
        }
        return emptyList()
    }

    override suspend fun postMultipleChoiceResponse(multipleChoiceResponseData: MultipleChoiceResponseData): MultipleChoiceResponseData {
        try {
            val response = apiService.postMultipleChoiceResponse(multipleChoiceResponseData)
            if (response.isSuccessful && response.body() != null) {
                return response.body()!!
            }
        } catch (e: Exception) {
            Log.e(
                TAG,
                "Error posting multiple choice response: ${e.message}"
            )
        }
        return multipleChoiceResponseData
    }
}