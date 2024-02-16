package com.example.learnit.ui.feature.courses.courses.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.data.courses.chapters.model.ChapterWithLessonsData
import com.example.learnit.domain.course.ChaptersRepository
import com.example.learnit.ui.App
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChaptersViewModel : ViewModel() {
    private val repository: ChaptersRepository = App.instance.getChaptersRepository()

    private val mutableState = MutableStateFlow<ChaptersScreenState>(ChaptersScreenState.Loading)
    val state: StateFlow<ChaptersScreenState> = mutableState

    companion object {
        val TAG: String = ChaptersViewModel::class.java.simpleName
    }

    sealed class ChaptersScreenState {
        data object Loading : ChaptersScreenState()
        data class Success(val chaptersData: List<ChapterWithLessonsData>) : ChaptersScreenState()
        data class Failure(val throwable: Throwable) : ChaptersScreenState()
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = ChaptersScreenState.Failure(exception)
    }

    fun loadChapters(courseId: Int) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val loadedChapters = repository.getChaptersByCourseId(courseId)
                mutableState.value = ChaptersScreenState.Success(loadedChapters)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching chapters: ${e.message}")
                mutableState.value = ChaptersScreenState.Failure(e)
            }
        }
    }

}