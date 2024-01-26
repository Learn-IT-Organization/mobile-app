package com.example.learnit.ui.feature.courses.quiz.fragment

import QuizResponseModel
import UserResponseModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.learnit.data.SharedPreferences
import com.example.learnit.databinding.FragmentQuizMultipleChoiceBinding
import com.example.learnit.ui.feature.courses.quiz.QuizPagerAdapter
import com.example.learnit.ui.feature.courses.quiz.model.QuestionsAnswersModel
import com.example.learnit.ui.feature.courses.quiz.viewModel.SharedQuizViewModel
import kotlinx.coroutines.launch
import java.util.Date

class MultipleChoiceQuizFragment : BaseQuizFragment(), QuizPagerAdapter.QuizButtonClickListener {
    override lateinit var binding: FragmentQuizMultipleChoiceBinding

    override val viewModel: SharedQuizViewModel by viewModels()
    override val TAG: String = MultipleChoiceQuizFragment::class.java.simpleName
    private var currentQuestion: QuestionsAnswersModel? = null

    private var courseId: Int = -1
    private var chapterId: Int = -1
    private var lessonId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizMultipleChoiceBinding.inflate(inflater, container, false)
        courseId = arguments?.getInt("courseId", -1) ?: -1
        chapterId = arguments?.getInt("chapterId", -1) ?: -1
        lessonId = arguments?.getInt("lessonId", -1) ?: -
        Log.d(TAG, "MCAdatok: $courseId $chapterId $lessonId")
        viewModel.loadAllQuestionsAnswers(courseId, chapterId, lessonId)
        lessonId = arguments?.getInt("lessonId", -1) ?: -1
        return binding.root
    }

    override fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is SharedQuizViewModel.QuestionAnswersPageState.Loading -> {
                            Log.d(TAG, "Loading MultipleChoice questionsAnswers...")
                        }

                        is SharedQuizViewModel.QuestionAnswersPageState.Success -> {
                            Log.d(TAG, "MultipleChoice QuestionsAnswers loaded")
                            currentQuestion =
                                viewModel.shuffleAndSelectQuestion("multiple_choice")
                            binding.questionTextView.text = currentQuestion?.questionText
                            val answers = currentQuestion?.answers
                            if (answers != null) {
                                if (answers.isNotEmpty()) {
                                    val answerTextViews = listOf(
                                        binding.option1CheckBox,
                                        binding.option2CheckBox,
                                        binding.option3CheckBox,
                                        binding.option4CheckBox,
                                    )
                                    for ((index, answer) in answers.withIndex()) {
                                        answerTextViews[index].text = answer
                                    }
                                }
                            }
                        }

                        is SharedQuizViewModel.QuestionAnswersPageState.Failure -> {
                            Log.e(
                                TAG,
                                "Error loading MultipleChoice QuestionsAnswers: ${state.throwable}"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun clearCheckBoxes() {
        val answerCheckBoxes = listOf(
            binding.option1CheckBox,
            binding.option2CheckBox,
            binding.option3CheckBox,
            binding.option4CheckBox,
        )

        for (checkBox in answerCheckBoxes) {
            checkBox.isChecked = false
        }
    }

    override fun onNextButtonClicked() {
        viewModel.submitMultipleChoiceResponse(
            UserResponseModel(
                uqrQuestionId = currentQuestion?.questionId ?: -1,
                uqrUserId = SharedPreferences.getUserId().toInt(),
                response = QuizResponseModel(getSelectedAnswers()),
                responseTime = Date(),
                score = 0.0f
            )
        )
        Log.d(TAG, "question id:${currentQuestion?.questionId}")
        Log.d(TAG, "multiple_choice onNextButtonClicked")
        clearCheckBoxes()
    }

    private fun getSelectedAnswers(): List<Boolean> {
        val selectedAnswers = mutableListOf<Boolean>()

        val answerCheckBoxes = listOf(
            binding.option1CheckBox,
            binding.option2CheckBox,
            binding.option3CheckBox,
            binding.option4CheckBox,
        )

        for (checkBox in answerCheckBoxes) {
            selectedAnswers.add(checkBox.isChecked)
        }
        Log.d(TAG, "getSelectedAnswers: $selectedAnswers")
        return selectedAnswers
    }
}