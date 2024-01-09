package com.example.learnit.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.learnit.data.courses.quiz.mapper.mapToAnswerData
import com.example.learnit.data.courses.quiz.model.AnswerData
import com.example.learnit.data.courses.quiz.model.UserAnswerData
import com.example.learnit.data.courses.quiz.model.UserResponseData
import com.example.learnit.data.courses.quiz.repository.QuizResultRepositoryImpl
import com.example.learnit.databinding.FragmentMultipleChoiceBinding
import com.example.learnit.ui.feature.courses.quiz.model.AnswerModel
import com.example.learnit.ui.feature.courses.quiz.model.QuestionsAnswersModel
import com.example.learnit.ui.feature.courses.quiz.viewModel.MultipleChoiceQuestionAnswerViewModel
import kotlinx.coroutines.launch
import java.util.Date

class MultipleChoiceQuizFragment : Fragment() {
    private val viewModel: MultipleChoiceQuestionAnswerViewModel by viewModels()

    private lateinit var binding: FragmentMultipleChoiceBinding

    private var courseId: Int = -1
    private var chapterId: Int = -1
    private var lessonId: Int = -1

    private var currentQuizIndex: Int = 0
    private var quizzes: List<QuestionsAnswersModel<AnswerModel>> = emptyList()

    companion object {
        val TAG: String = MultipleChoiceQuizFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMultipleChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        courseId = arguments?.getInt("courseId", -1) ?: -1
        chapterId = arguments?.getInt("chapterId", -1) ?: -1
        lessonId = arguments?.getInt("lessonId", -1) ?: -1
        observeState()

        viewModel.loadMultipleChoice(courseId, chapterId, lessonId)
        //setUpSubmitButton()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is MultipleChoiceQuestionAnswerViewModel.MultipleQuestionPageState.Loading -> {
                            Log.d(TAG, "Loading multiple choice questions...")
                        }

                        is MultipleChoiceQuestionAnswerViewModel.MultipleQuestionPageState.Success -> {
                            Log.d(TAG, "Multiple choice questions loaded")
                            quizzes = state.multipleChoiceData
                            updateUI(quizzes)
                        }

                        is MultipleChoiceQuestionAnswerViewModel.MultipleQuestionPageState.Failure -> {
                            Log.e(
                                TAG,
                                "Error loading multiple choice questions: ${state.throwable}"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateUI(multipleChoiceData: List<QuestionsAnswersModel<AnswerModel>>) {
        if (multipleChoiceData.isNotEmpty()) {
            val currentQuiz = multipleChoiceData[currentQuizIndex]
            binding.questionTextView.text = currentQuiz.questionText

            val answers = currentQuiz.answers

            if (answers.isNotEmpty()) {
                val answerTextViews = listOf(
                    binding.option1CheckBox,
                    binding.option2CheckBox,
                    binding.option3CheckBox,
                    binding.option4CheckBox,
                    binding.option5CheckBox
                )

                for ((index, answer) in answers.withIndex()) {
                    answerTextViews[index].text = answer.optionText
                }
            }
        }
    }

    private fun loadNextQuiz() {
        clearCheckBoxes()

        currentQuizIndex++
        if (currentQuizIndex < quizzes.size) {
            updateUI(quizzes)
        } else {
            Toast.makeText(context, "No more quizzes available.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleUserAnswers() {
        val selectedAnswers = getSelectedAnswers()
        val questionsAnswersData = createQuizResponseData(selectedAnswers)
        Log.d(TAG, "handleUserAnswers: $questionsAnswersData")

        lifecycleScope.launch {
            try {
                val response =
                    QuizResultRepositoryImpl.sendResponse(questionsAnswersData)
                if (response.success) {
                    val totalQuestions = questionsAnswersData.response.size
                    //val grade = (correctResponses.toDouble() / totalQuestions * 100).toInt()

//                    Toast.makeText(
//                        context,
//                        "User response recorded successfully. Grade: $grade%",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    updateScore(grade)
                } else {
                    Toast.makeText(context, "Failed to record user response.", Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error sending quiz response: ${e.message}")
                Toast.makeText(context, "Error sending quiz response.", Toast.LENGTH_SHORT).show()
            } finally {
                loadNextQuiz()
            }
        }
    }

    private fun createQuizResponseData(selectedAnswers: List<AnswerData>): UserResponseData {
        val currentQuiz = quizzes[currentQuizIndex]
        return UserResponseData(
            uqr_question_id = currentQuiz.questionId,
            uqr_user_id = 4,
            response = listOf(
                UserAnswerData(
                    optionText = if (viewModel.getUserResponse().optionText.isEmpty()) {
                        selectedAnswers[0].option_text
                    } else {
                        viewModel.getUserResponse().optionText
                    },
                    isCorrect = viewModel.getUserResponse().isCorrect
                )
            ),
            is_correct = 1,
            score = 1,
            response_time = Date()
        )
    }

//    private fun setUpSubmitButton() {
//        binding.submitButton.setOnClickListener {
//            handleUserAnswers()
//        }
//    }

    private fun getSelectedAnswers(): List<AnswerData> {
        val selectedAnswers = mutableListOf<AnswerData>()

        val answerTextViews = listOf(
            binding.option1CheckBox,
            binding.option2CheckBox,
            binding.option3CheckBox,
            binding.option4CheckBox,
            binding.option5CheckBox
        )

        for ((index, textView) in answerTextViews.withIndex()) {
            if (textView.isChecked) {
                val answerText = textView.text.toString()
                val isCorrect = quizzes[currentQuizIndex].answers[index].isCorrect
                selectedAnswers.add(AnswerModel(answerText, isCorrect).mapToAnswerData())
            }
        }

        return selectedAnswers
    }

    private fun AnswerData.mapToAnswerModel(): AnswerModel {
        return AnswerModel(optionText = option_text, isCorrect = is_correct)
    }


    private fun updateScore(newScore: Int) {
        Log.d(TAG, "Score updated: $newScore")
    }

    private fun clearCheckBoxes() {
        val answerCheckBoxes = listOf(
            binding.option1CheckBox,
            binding.option2CheckBox,
            binding.option3CheckBox,
            binding.option4CheckBox,
            binding.option5CheckBox
        )

        for (checkBox in answerCheckBoxes) {
            checkBox.isChecked = false
        }
    }

}