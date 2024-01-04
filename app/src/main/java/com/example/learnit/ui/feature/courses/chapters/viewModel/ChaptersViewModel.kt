package com.example.learnit.ui.feature.courses.chapters.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.domain.chapters.repository.ChaptersRepository
import com.example.learnit.ui.App
import com.example.learnit.ui.feature.courses.chapters.model.ChapterModel
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
        val TAG = ChaptersViewModel::class.java.simpleName
    }

    sealed class ChaptersScreenState {
        //data object
        object Loading : ChaptersScreenState()
        data class Success(val chaptersData: List<ChapterModel>) : ChaptersScreenState()
        data class Failure(val throwable: Throwable) : ChaptersScreenState()
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = ChaptersScreenState.Failure(exception)
    }

//    init {
//        courseId?.let { loadChapters(it.toInt()) }
//    }

    fun loadChapters(courseId: Int) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val loadedChapters = repository.getChaptersByCourseId(courseId)
                mutableState.value = ChaptersScreenState.Success(loadedChapters)
                Log.d(TAG, loadedChapters.toString())
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching chapters: ${e.message}")
                mutableState.value = ChaptersScreenState.Failure(e)
            }
        }
    }
}