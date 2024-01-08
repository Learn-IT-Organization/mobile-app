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
import com.example.learnit.databinding.FragmentMultipleChoiceBinding
import com.example.learnit.ui.feature.courses.quiz.model.MultipleChoiceQuestionAnswerModel
import com.example.learnit.ui.feature.courses.quiz.model.MultipleChoiceResponseModel
import com.example.learnit.ui.feature.courses.quiz.model.Response
import com.example.learnit.ui.feature.courses.quiz.model.ResponseAnswer
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
    private var quizzes: List<MultipleChoiceQuestionAnswerModel> = emptyList()

    companion object {
        val TAG: String = MultipleChoiceQuizFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        setUpSubmitButton()
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

    private fun updateUI(multipleChoiceData: List<MultipleChoiceQuestionAnswerModel>) {
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
                    answerTextViews[index].text = answer.ansText
                }
            }
        }
    }

    private fun loadNextQuiz() {
        currentQuizIndex++
        if (currentQuizIndex < quizzes.size) {
            updateUI(quizzes)
        } else {
            Toast.makeText(context, "No more quizzes available.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleUserAnswers() {
        val selectedAnswers = getSelectedAnswers()
        val userResponse = createMultipleChoiceResponse(selectedAnswers)

        viewModel.submitMultipleChoiceResponse(userResponse)
        loadNextQuiz()
    }

    private fun setUpSubmitButton() {
        binding.submitButton.setOnClickListener {
            handleUserAnswers()
        }
    }

    private fun getSelectedAnswers(): List<ResponseAnswer> {
        val selectedAnswers = mutableListOf<ResponseAnswer>()

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
                selectedAnswers.add(ResponseAnswer(answerText, isCorrect))
            }
        }

        return selectedAnswers
    }

    private fun createMultipleChoiceResponse(selectedAnswers: List<ResponseAnswer>): MultipleChoiceResponseModel {
        //just for testing
        return MultipleChoiceResponseModel(
            uqrQuestionId = quizzes[currentQuizIndex].questionId,
            uqrUserId = 1,
            response = Response(selectedAnswers),
            isCorrect = false,
            score = 0f,
            responseTime = Date()
        )
    }
}