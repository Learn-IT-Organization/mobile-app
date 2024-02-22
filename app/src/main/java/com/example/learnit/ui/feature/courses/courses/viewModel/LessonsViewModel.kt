package com.example.learnit.ui.feature.courses.courses.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.data.courses.lessons.model.LessonProgressData
import com.example.learnit.domain.course.LessonRepository
import com.example.learnit.ui.App
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LessonsViewModel : ViewModel() {
    private val repository: LessonRepository = App.instance.getLessonRepository()

    private val mutableState = MutableStateFlow<LessonScreenState>(LessonScreenState.Loading)
    val state: StateFlow<LessonScreenState> = mutableState

    companion object {
        val TAG: String = LessonsViewModel::class.java.simpleName
    }

    sealed class LessonScreenState {
        data object Loading : LessonScreenState()
        data class Success(val lessonResultData: List<LessonProgressData>) : LessonScreenState()
        data class Failure(val throwable: Throwable) : LessonScreenState()
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
                Log.e(TAG, "Error fetching lesson: ${e.message}")
            }
        }
    }
}