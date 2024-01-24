package com.example.learnit.ui.feature.courses.quiz.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.domain.quiz.repository.QuestionsAnswersRepository
import com.example.learnit.ui.App
import com.example.learnit.ui.feature.courses.quiz.model.QuestionsAnswersModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedQuizViewModel : ViewModel() {

    private val repository: QuestionsAnswersRepository =
        App.instance.getQuestionsAnswersRepository()

    private val mutableState =
        MutableStateFlow<QuestionAnswersPageState>(QuestionAnswersPageState.Loading)
    val state: StateFlow<QuestionAnswersPageState> = mutableState

    private var loadedQuestionsAnswers: List<QuestionsAnswersModel>? = null
    private var userResponse: Boolean? = null
    private var isResponseSet = false

    companion object {
        val TAG: String = SharedQuizViewModel::class.java.simpleName
    }

    sealed class QuestionAnswersPageState {
        object Loading : QuestionAnswersPageState()
        data class Success(val questionsAnswersData: List<QuestionsAnswersModel>) :
            QuestionAnswersPageState()

        data class Failure(val throwable: Throwable) : QuestionAnswersPageState()
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = QuestionAnswersPageState.Failure(exception)
    }

    fun loadAllQuestionsAnswers(
        courseId: Int,
        chapterId: Int,
        lessonId: Int) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                loadedQuestionsAnswers =
                    repository.getQuestionsAnswersByCourseIdChapterIdLessonId(
                        courseId,
                        chapterId,
                        lessonId
                    )
                mutableState.value = SharedQuizViewModel.QuestionAnswersPageState.Success(loadedQuestionsAnswers!!)
                Log.d(TAG, "loadedQuestionsAnswers: $loadedQuestionsAnswers")
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching lessons: ${e.message}")
                mutableState.value = SharedQuizViewModel.QuestionAnswersPageState.Failure(e)
            }
        }
    }

    fun shuffleAndSelectQuestion(type: String): QuestionsAnswersModel? {
        loadedQuestionsAnswers?.let { questions ->
            val filteredQuestions = questions.filter { it.questionType == type }
            return filteredQuestions.shuffled().firstOrNull()
        }
        return null
    }

    fun setUserResponse(isTrue: Boolean) {
        userResponse = isTrue
        isResponseSet = true
    }
    fun getUserResponse(): Boolean? {
        return userResponse
    }

    fun isResponseSet(): Boolean {
        return isResponseSet
    }

}