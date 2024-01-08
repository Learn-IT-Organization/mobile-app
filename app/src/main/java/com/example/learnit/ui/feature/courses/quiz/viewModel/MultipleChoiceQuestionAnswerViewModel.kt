package com.example.learnit.ui.feature.courses.quiz.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.data.courses.quiz.mapper.mapToResponse
import com.example.learnit.domain.course.repository.LessonRepository
import com.example.learnit.ui.App
import com.example.learnit.ui.feature.courses.quiz.model.MultipleChoiceQuestionAnswerModel
import com.example.learnit.ui.feature.courses.quiz.model.MultipleChoiceResponseModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MultipleChoiceQuestionAnswerViewModel : ViewModel() {
    private val repository: LessonRepository = App.instance.getLessonRepository()

    private val mutableState =
        MutableStateFlow<MultipleQuestionPageState>(MultipleQuestionPageState.Loading)
    val state: StateFlow<MultipleQuestionPageState> = mutableState

    companion object {
        val TAG: String = MultipleChoiceQuestionAnswerViewModel::class.java.simpleName
    }

    sealed class MultipleQuestionPageState {
        data object Loading : MultipleQuestionPageState()
        data class Success(val multipleChoiceData: List<MultipleChoiceQuestionAnswerModel>) :
            MultipleQuestionPageState()

        data class Failure(val throwable: Throwable) : MultipleQuestionPageState()
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = MultipleQuestionPageState.Failure(exception)
    }

    fun loadMultipleChoice(courseId: Int, chapterId: Int, lessonId: Int) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val loadedMultipleChoiceQuestionAnswers =
                    repository.getQuestionsAnswersByCourseIdChapterIdLessonIdMultipleChoice(
                        courseId,
                        chapterId,
                        lessonId
                    )
                Log.d(
                    TAG,
                    "loadedMultipleChoiceQuestionAnswers: $loadedMultipleChoiceQuestionAnswers"
                )
                mutableState.value =
                    MultipleQuestionPageState.Success(loadedMultipleChoiceQuestionAnswers)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching multiple choice answers: ${e.message}")
                mutableState.value = MultipleQuestionPageState.Failure(e)
            }
        }
    }

    fun submitMultipleChoiceResponse(response: MultipleChoiceResponseModel) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                repository.postMultipleChoiceResponse(response.mapToResponse())
            } catch (e: Exception) {
                Log.e(TAG, "Error submitting multiple choice response: ${e.message}")
            }
        }
    }

}