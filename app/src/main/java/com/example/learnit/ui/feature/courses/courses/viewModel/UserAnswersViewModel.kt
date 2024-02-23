package com.example.learnit.ui.feature.courses.courses.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.data.courses.lessons.model.UserAnswersData
import com.example.learnit.domain.course.LessonRepository
import com.example.learnit.ui.App
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserAnswersViewModel : ViewModel() {
    private val repository: LessonRepository = App.instance.getLessonRepository()

    private val mutableState =
        MutableStateFlow<UserAnswersScreenState>(UserAnswersScreenState.Loading)
    val state: StateFlow<UserAnswersScreenState> = mutableState

    companion object {
        val TAG: String = UserAnswersViewModel::class.java.simpleName
    }

    sealed class UserAnswersScreenState {
        data object Loading : UserAnswersScreenState()
        data class Success(val userAnswersData: List<UserAnswersData>) : UserAnswersScreenState()
        data class Failure(val throwable: Throwable) : UserAnswersScreenState()
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = UserAnswersScreenState.Failure(exception)
    }

    fun loadUserAnswers(lessonId: Int) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val loadedUserAnswers = repository.getLessonResultWithValidation(lessonId)
                Log.d(TAG, "User answers: $loadedUserAnswers")
                mutableState.value = UserAnswersScreenState.Success(loadedUserAnswers)
            } catch (e: Exception) {
                mutableState.value = UserAnswersScreenState.Failure(e)
            }
        }
    }
}