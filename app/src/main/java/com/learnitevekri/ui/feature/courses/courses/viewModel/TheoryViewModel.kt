package com.learnitevekri.ui.feature.courses.courses.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnitevekri.data.courses.lessons.model.LessonContentData
import com.learnitevekri.data.courses.lessons.model.LessonData
import com.learnitevekri.domain.course.LessonRepository
import com.learnitevekri.ui.App
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TheoryViewModel : ViewModel() {
    private val repository: LessonRepository = App.instance.getLessonRepository()

    private val mutableState = MutableStateFlow<TheoryPageState>(TheoryPageState.Loading)
    val state: StateFlow<TheoryPageState> = mutableState

    private val mutableState2 = MutableStateFlow<TheoryPageStateForLesson>(TheoryPageStateForLesson.Loading)
    val state2: StateFlow<TheoryPageStateForLesson> = mutableState2

    companion object {
        val TAG: String = TheoryViewModel::class.java.simpleName
    }

    sealed class TheoryPageState {
        data object Loading : TheoryPageState()
        data class Success(val lessonContentData: List<LessonContentData>) : TheoryPageState()
        data class Failure(val throwable: Throwable) : TheoryPageState()
    }
    sealed class TheoryPageStateForLesson {
        data object Loading : TheoryPageStateForLesson()
        data class Success(val lessonData: LessonData?) : TheoryPageStateForLesson()
        data class Failure(val throwable: Throwable) : TheoryPageStateForLesson()
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = TheoryPageState.Failure(exception)
        mutableState2.value = TheoryPageStateForLesson.Failure(exception)
    }

    fun loadLessonContents(lessonId: Int) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val loadedContents = repository.getLessonContentByLessonId(lessonId)
                Log.d(TAG, "Loaded lesson contents: $loadedContents")
                mutableState.value = TheoryPageState.Success(loadedContents)
            } catch (e: Exception) {
                Log.e(ChaptersViewModel.TAG, "Error fetching chapters: ${e.message}")
                mutableState.value = TheoryPageState.Failure(e)
            }
        }
    }

    fun loadLessonById(lessonId: Int) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val loadedLesson = repository.getLessonById(lessonId)
                Log.d(TAG, "Loaded lesson contents: $loadedLesson")
                mutableState2.value = TheoryPageStateForLesson.Success(loadedLesson)
            } catch (e: Exception) {
                Log.e(ChaptersViewModel.TAG, "Error fetching chapters: ${e.message}")
                mutableState.value = TheoryPageState.Failure(e)
            }
        }
    }
}