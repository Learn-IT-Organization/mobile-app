package com.learnitevekri.ui.feature.courses.courses.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnitevekri.data.courses.lessons.model.AddLessonContentResponseData
import com.learnitevekri.data.courses.lessons.model.EditLessonContentData
import com.learnitevekri.data.courses.lessons.model.LessonContentData
import com.learnitevekri.domain.course.LessonRepository
import com.learnitevekri.ui.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddContentViewModel : ViewModel() {

    val repository: LessonRepository = App.instance.getLessonRepository()
    var contentButtonVisibilityAndEditTextMap: MutableMap<Int, Boolean> = mutableMapOf()

    companion object {
        val TAG: String = AddContentViewModel::class.java.simpleName
    }

    sealed class AddContentScreenState {
        data object Loading : AddContentScreenState()
        data class Success(val response: Int?) : AddContentScreenState()
        data class Failure(val throwable: Throwable) : AddContentScreenState()
    }

    private val mutableState =
        MutableStateFlow<AddContentScreenState>(AddContentScreenState.Loading)
    val state: StateFlow<AddContentScreenState> = mutableState

    sealed class EditContentScreenState {
        data object Loading : EditContentScreenState()
        data class Success(val response: AddLessonContentResponseData) : EditContentScreenState()
        data class Failure(val throwable: Throwable) : EditContentScreenState()
    }

    private val mutableEditState =
        MutableStateFlow<EditContentScreenState>(EditContentScreenState.Loading)

    suspend fun createLessonContent(lessonContentData: LessonContentData): Int? {
        return withContext(Dispatchers.IO) {
            try {
                repository.createLessonContent(lessonContentData)
            } catch (e: Exception) {
                Log.e(TAG, "Error adding new content: ${e.message}")
                null
            }
        }
    }

    fun editLessonContent(lessonContentId: Int, lessonContentData: EditLessonContentData) {
        viewModelScope.launch {
            try {
                val response = repository.editLessonContent(lessonContentId, lessonContentData)
                mutableEditState.value = EditContentScreenState.Success(response)
            } catch (e: Exception) {
                mutableState.value = AddContentScreenState.Failure(e)
            }
        }
    }
}