package com.learnitevekri.ui.feature.courses.courses.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnitevekri.data.courses.lessons.model.LessonContentData
import com.learnitevekri.domain.course.LessonRepository
import com.learnitevekri.ui.App
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddContentViewModel : ViewModel() {

    val repository: LessonRepository = App.instance.getLessonRepository()

    companion object {
        val TAG: String = AddContentViewModel::class.java.simpleName
    }

    sealed class AddContentScreenState {
        data object Loading : AddContentScreenState()
        data class Success(val response: LessonContentData) : AddContentScreenState()
        data class Failure(val throwable: Throwable) : AddContentScreenState()
    }

    private val mutableState =
        MutableStateFlow<AddContentScreenState>(AddContentScreenState.Loading)
    val state: StateFlow<AddContentScreenState> = mutableState

    fun createLessonContent(lessonContentData: LessonContentData) {
        viewModelScope.launch {
            try {
                val response = repository.createLessonContent(lessonContentData)
                mutableState.value = AddContentScreenState.Success(response)
            } catch (e: Exception) {
                mutableState.value = AddContentScreenState.Failure(e)
            }
        }
    }

}