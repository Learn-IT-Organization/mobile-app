package com.learnitevekri.ui.feature.courses.courses.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnitevekri.data.courses.lessons.model.AddNewLessonData
import com.learnitevekri.data.courses.lessons.model.AddNewLessonResponseData
import com.learnitevekri.data.courses.lessons.model.EditLessonData
import com.learnitevekri.data.courses.lessons.model.LessonData
import com.learnitevekri.data.courses.lessons.model.LessonProgressData
import com.learnitevekri.domain.course.LessonRepository
import com.learnitevekri.ui.App
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class   LessonsViewModel : ViewModel() {
    private val repository: LessonRepository = App.instance.getLessonRepository()

    private val mutableState = MutableStateFlow<LessonScreenState>(LessonScreenState.Loading)
    val state: StateFlow<LessonScreenState> = mutableState

    private val mutableState2 = MutableStateFlow<EditLessonState>(EditLessonState.Loading)
    val state2: StateFlow<EditLessonState> = mutableState2

    companion object {
        val TAG: String = LessonsViewModel::class.java.simpleName
    }

    sealed class LessonScreenState {
        data object Loading : LessonScreenState()
        data class Success(val lessonResultData: List<LessonProgressData>) : LessonScreenState()
        data class Failure(val throwable: Throwable) : LessonScreenState()
    }

    sealed class EditLessonState {
        object Loading : EditLessonState()
        data class Success(val lessonData: LessonData) : EditLessonState()
        data class Updated(val response: AddNewLessonResponseData) : EditLessonState()
        data class Failure(val throwable: Throwable) : EditLessonState()
    }


    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = LessonScreenState.Failure(exception)
    }

    fun loadLessonResult() {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val loadedLessonResult = repository.getLessonProgress()
                Log.d(TAG, "Lesson result: $loadedLessonResult")
                mutableState.value = LessonScreenState.Success(loadedLessonResult)
            } catch (e: Exception) {
                mutableState.value = LessonScreenState.Failure(e)
            }
        }
    }

    fun getLessonById(lessonId: Int) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val lesson = repository.getLessonById(lessonId)
                Log.d(TAG, "Lesson: $lesson")
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching lesson by id: ${e.message}")
            }
        }
    }

    suspend fun addNewLesson(addNewLessonData: AddNewLessonData): Int? {
        return withContext(Dispatchers.IO) {
            try {
                repository.addNewLesson(addNewLessonData)
            } catch (e: Exception) {
                Log.e(TAG, "Error adding new lesson: ${e.message}")
                null
            }
        }
    }

    fun loadLessonById(lessonId: Int) {
        viewModelScope.launch {
            try {
                val lesson = repository.getLessonById(lessonId)
                mutableState2.value = EditLessonState.Success(lesson)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching lesson by id: ${e.message}")
                mutableState2.value = EditLessonState.Failure(e)
            }
        }
    }

    suspend fun editLesson(
        lessonId: Int, editLessonData: EditLessonData
    ): AddNewLessonResponseData {
        return withContext(Dispatchers.IO) {
            try {
                repository.editLesson(lessonId, editLessonData).also {
                    Log.d(TAG, "Lesson updated successfully: $lessonId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating lesson: ${e.message}")
                throw e
            }
        }
    }
}