package com.example.learnit.ui.feature.courses.quiz.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.data.courses.quiz.model.UserResponseData
import com.example.learnit.domain.quiz.repository.QuestionsAnswersRepository
import com.example.learnit.domain.quiz.repository.QuizResultRepository
import com.example.learnit.ui.App
import com.example.learnit.ui.feature.courses.quiz.model.AnswerModel
import com.example.learnit.ui.feature.courses.quiz.model.QuestionsAnswersModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TrueFalseQuizViewModel : ViewModel() {
    private val repository: QuestionsAnswersRepository =
        App.instance.getQuestionsAnswersRepository()

    private val quizResultRepository: QuizResultRepository =
        App.instance.getQuizResultRepository()

    private val mutableState =
        MutableStateFlow<QuestionAnswersPageState>(QuestionAnswersPageState.Loading)

    val state: StateFlow<QuestionAnswersPageState> = mutableState

    var currentQuestion: QuestionsAnswersModel<AnswerModel>? = null

    private var userResponse: Boolean? = null

    private var isResponseSet = false

    companion object {
        val TAG: String = TrueFalseQuizViewModel::class.java.simpleName
    }

    sealed class QuestionAnswersPageState {
        object Loading : QuestionAnswersPageState()
        data class Success(val questionsAnswersData: List<QuestionsAnswersModel<AnswerModel>>) :
            QuestionAnswersPageState()

        data class Failure(val throwable: Throwable) : QuestionAnswersPageState()
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = QuestionAnswersPageState.Failure(exception)
    }

    fun loadQuestionsAnswers() {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val loadedQuestionsAnswers = repository.getQuestionsAnswers()
                currentQuestion = loadedQuestionsAnswers
                    .shuffled().firstOrNull { it.questionType == "true_false" }
                Log.d(TAG, "randomQuestionAnswer: $currentQuestion")
                mutableState.value = QuestionAnswersPageState.Success(loadedQuestionsAnswers)
                Log.d(TAG, "loadedQuestionsAnswers: $loadedQuestionsAnswers")
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching lessons: ${e.message}")
                mutableState.value = QuestionAnswersPageState.Failure(e)
            }
        }
    }

    fun sendUserResponse(quizResultModel: UserResponseData) {
        Log.d(TAG, "sendUserResponse: $quizResultModel")
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            val response = quizResultRepository.sendResponse(quizResultModel)
            Log.d(TAG, "send UserResponse: $response")
        }
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

    fun resetUserResponse() {
        userResponse = null
        isResponseSet = false
    }
}