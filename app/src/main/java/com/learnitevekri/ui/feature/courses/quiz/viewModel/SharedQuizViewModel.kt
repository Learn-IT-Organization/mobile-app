package com.learnitevekri.ui.feature.courses.quiz.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnitevekri.data.courses.quiz.model.BaseQuestionData
import com.learnitevekri.data.courses.quiz.model.QuizResponseData
import com.learnitevekri.domain.quiz.QuestionsAnswersRepository
import com.learnitevekri.domain.quiz.QuizResultRepository
import com.learnitevekri.ui.App
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


    private var loadedQuestionsAnswers: List<BaseQuestionData> = emptyList()
    private var userResponse: Boolean = false
    private var isResponseSet = false

    val currentQuestionItemLiveData: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }

    private val mutableState: MutableStateFlow<QuestionAnswersPageState> =
        MutableStateFlow(QuestionAnswersPageState.Loading)

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = QuestionAnswersPageState.Failure(exception)
    }

    val scoreLiveData: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>()
    }

    val soundLiveData: MutableLiveData<Boolean?> by lazy {
        MutableLiveData<Boolean?>()
    }

    val state: StateFlow<QuestionAnswersPageState> = mutableState

    sealed class QuestionAnswersPageState {
        data object Loading : QuestionAnswersPageState()
        data class Success(val questionsAnswersData: List<BaseQuestionData>) :
            QuestionAnswersPageState()

        data class Failure(val throwable: Throwable) : QuestionAnswersPageState()
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

    fun sendUserResponse(userResponse: QuizResponseData) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            val response = quizResultRepository.sendResponse(userResponse)
            scoreLiveData.postValue(response.score)
            Log.d(TAG, response.score.toString())

            val soundEnabled = when (response.score) {
                0.0f -> false
                else -> true
            }

            soundLiveData.postValue(soundEnabled)
        }
    }

    fun deleteResponses(lessonId: Int) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            quizResultRepository.deleteResponses(lessonId)
        }
    }
}