package com.example.learnit.ui.feature.courses.chapters.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.data.courses.lessons.model.LessonContentData
import com.example.learnit.data.courses.lessons.model.LessonData
import com.example.learnit.domain.course.repository.LessonRepository
import com.example.learnit.ui.App
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
        data class Success(val lessonContentData: List<LessonContentData>) : TheoryPageState()
        data class Failure(val throwable: Throwable) : TheoryPageState()
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = TheoryPageState.Failure(exception)
    }

    fun loadLessonContents(lessonId: Int) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val loadedContents = repository.getLessonContentByLessonId(lessonId)
                Log.d(TAG, "Loaded lesson contents: $loadedContents")
                mutableState.value = TheoryViewModel.TheoryPageState.Success(loadedContents)
            } catch (e: Exception) {
                Log.e(ChaptersViewModel.TAG, "Error fetching chapters: ${e.message}")
                mutableState.value = TheoryViewModel.TheoryPageState.Failure(e)
            }
        }
    }
}