package com.example.learnit.ui.feature.courses.lessons.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.domain.course.repository.LessonRepository
import com.example.learnit.ui.App
import com.example.learnit.ui.feature.courses.lessons.model.LessonContentModel
import com.example.learnit.ui.feature.courses.lessons.model.LessonModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TheoryViewModel : ViewModel() {
    private val repository: LessonRepository = App.instance.getLessonRepository()

    private val mutableState = MutableStateFlow<TheoryPageState>(TheoryPageState.Loading)
    val state: StateFlow<TheoryPageState> = mutableState

    companion object {
        val TAG: String = TheoryViewModel::class.java.simpleName
    }

    sealed class TheoryPageState {
        data object Loading : TheoryPageState()
        data class Success(val lessonContentData: List<LessonModel>) : TheoryPageState()
        data class Failure(val throwable: Throwable) : TheoryPageState()
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = TheoryPageState.Failure(exception)
    }

    fun loadLessonContents(lessonId: Int) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val loadedLessons = repository.getLessons()
                Log.d(TAG, "loadedLessonContents: $loadedLessons")
                mutableState.value = TheoryPageState.Success(loadedLessons)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching lesson contents: ${e.message}")
                mutableState.value = TheoryPageState.Failure(e)
            }
        }
    }
}