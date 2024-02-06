package com.example.learnit.ui.feature.courses.quiz.viewModel

import UserResponseModel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.data.courses.quiz.mapper.mapToUserResponseData
import com.example.learnit.data.courses.quiz.model.UserResponseData
import com.example.learnit.domain.quiz.repository.QuestionsAnswersRepository
import com.example.learnit.domain.quiz.repository.QuizResultRepository
import com.example.learnit.ui.App
import com.example.learnit.ui.feature.courses.quiz.model.QuestionsAnswersModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedQuizViewModel : ViewModel() {
    companion object {
        val TAG: String = SharedQuizViewModel::class.java.simpleName
    }

    private val repository: QuestionsAnswersRepository =
        App.instance.getQuestionsAnswersRepository()

    private val quizResultRepository: QuizResultRepository =
        App.instance.getQuizResultRepository()

    private var loadedQuestionsAnswers: List<QuestionsAnswersModel> = emptyList()
    private var userResponse: Boolean = false
    private var isResponseSet = false

    var numberOfQuestions: Int = 0

    val scoreLiveData: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>()
    }

    private val mutableState =
        MutableStateFlow<QuestionAnswersPageState>(QuestionAnswersPageState.Loading)

    val state: StateFlow<QuestionAnswersPageState> = mutableState

    sealed class QuestionAnswersPageState {
        data object Loading : QuestionAnswersPageState()
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
        lessonId: Int
    ) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                loadedQuestionsAnswers =
                    repository.getQuestionsAnswersByCourseIdChapterIdLessonId(
                        courseId,
                        chapterId,
                        lessonId
                    )
                mutableState.value =
                    QuestionAnswersPageState.Success(loadedQuestionsAnswers)

                Log.d(TAG, "loadedQuestionsAnswers: $loadedQuestionsAnswers")
                numberOfQuestions = loadedQuestionsAnswers.size
                Log.d(TAG, "numberOfQuestions: $numberOfQuestions")
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching questions: ${e.message}")
                mutableState.value = QuestionAnswersPageState.Failure(e)
            }
        }
    }

    fun setUserResponse(isTrue: Boolean) {
        userResponse = isTrue
        isResponseSet = true
    }

    fun getUserResponse(): Boolean {
        return userResponse
    }

    fun sendUserResponse(userResponse: UserResponseData) {
        Log.d(TAG, "User response: $userResponse")
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            val response = quizResultRepository.sendResponse(userResponse)
            Log.d(TAG, "Score: ${response.score}")
            scoreLiveData.postValue(response.score)
            Log.d(TAG, "Live score: ${scoreLiveData.value}")
        }
    }

    fun submitMultipleChoiceResponse(userResponse: UserResponseModel) {
        Log.d(TAG, "User response: $userResponse")
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            val response = quizResultRepository.sendResponse(userResponse.mapToUserResponseData())
            Log.d(TAG, "Score: ${response.score}")
            scoreLiveData.postValue(response.score)
            Log.d(TAG, "Live score: ${scoreLiveData.value}")
        }
    }

}