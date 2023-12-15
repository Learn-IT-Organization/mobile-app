package com.example.learnit.ui.feature.courses.lessons.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.domain.course.repository.LessonRepository
import com.example.learnit.ui.App
import com.example.learnit.ui.feature.courses.lessons.model.LessonModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LessonsViewModel : ViewModel() {
    private val repository: LessonRepository = App.instance.getLessonRepository()

    private val mutableState = MutableStateFlow<LessonsPageState>(LessonsPageState.Loading)
    val state: StateFlow<LessonsPageState> = mutableState

    companion object {
        val TAG = LessonsViewModel::class.java.simpleName
    }

    sealed class LessonsPageState {
        object Loading : LessonsPageState()
        data class Success(val lessonData: List<LessonModel>) : LessonsPageState()
        data class Failure(val throwable: Throwable) : LessonsPageState()
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = LessonsPageState.Failure(exception)
    }


    fun loadLessons(chapterId: Int) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val loadedLessons = repository.getLessonsByChapterId(chapterId)
                mutableState.value = LessonsPageState.Success(loadedLessons)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching lessons: ${e.message}")
                mutableState.value = LessonsPageState.Failure(e)
            }
        }
    }
}