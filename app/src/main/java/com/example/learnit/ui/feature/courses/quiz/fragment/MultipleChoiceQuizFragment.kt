package com.example.learnit.ui.feature.courses.quiz.fragment

import QuizResponseModel
import UserResponseModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.learnit.data.SharedPreferences
import com.example.learnit.databinding.FragmentMultipleChoiceBinding
import com.example.learnit.ui.feature.courses.quiz.QuizPagerAdapter
import com.example.learnit.ui.feature.courses.quiz.viewModel.MultipleChoiceQuizViewModel
import kotlinx.coroutines.launch
import java.util.Date

class MultipleChoiceQuizFragment : Fragment(), QuizPagerAdapter.QuizButtonClickListener {
    private val viewModel: MultipleChoiceQuizViewModel by viewModels()
    private lateinit var binding: FragmentMultipleChoiceBinding

    private var courseId: Int = -1
    private var chapterId: Int = -1
    private var lessonId: Int = -1

    companion object {
        val TAG: String = MultipleChoiceQuizFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMultipleChoiceBinding.inflate(inflater, container, false)
        courseId = arguments?.getInt("courseId", -1) ?: -1
        chapterId = arguments?.getInt("chapterId", -1) ?: -1
        lessonId = arguments?.getInt("lessonId", -1) ?: -1
        viewModel.loadMultipleChoice(courseId, chapterId, lessonId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is MultipleChoiceQuizViewModel.MultipleQuestionPageState.Loading -> {
                            Log.d(TAG, "Loading questionsAnswers...")
                        }

                        is MultipleChoiceQuizViewModel.MultipleQuestionPageState.Success -> {
                            Log.d(TAG, "QuestionsAnswers loaded")
                            Log.d(TAG, "randomQuestion: ${viewModel.currentQuestion}")
                            binding.questionTextView.text =
                                viewModel.currentQuestion?.questionText ?: ""
                            val answers = viewModel.currentQuestion?.answers
                            if (answers != null) {
                                if (answers.isNotEmpty()) {
                                    val answerTextViews = listOf(
                                        binding.option1CheckBox,
                                        binding.option2CheckBox,
                                        binding.option3CheckBox,
                                        binding.option4CheckBox,

                                        )
                                    for ((index, answer) in answers.withIndex()) {
                                        answerTextViews[index].text = answer.optionText
                                    }
                                }
                            }
                        }

                        is MultipleChoiceQuizViewModel.MultipleQuestionPageState.Failure -> {
                            Log.e(TAG, "Error loading QuestionsAnswers: ${state.throwable}")
                        }
                    }
                }
            }
        }
    }


    private fun getSelectedAnswers(): List<Boolean> {
        val answerCheckBoxes = listOf(
            binding.option1CheckBox,
            binding.option2CheckBox,
            binding.option3CheckBox,
            binding.option4CheckBox,
        )

        return answerCheckBoxes.map { checkBox ->
            checkBox.isChecked
        }
    }

    override fun onNextButtonClicked() {
        viewModel.submitMultipleChoiceResponse(
            UserResponseModel(
                uqrQuestionId = viewModel.currentQuestion?.questionId ?: -1,
                uqrUserId = SharedPreferences.getUserId().toInt(),
                response = QuizResponseModel(getSelectedAnswers()),
                responseTime = Date()
            )
        )
        clearCheckBoxes()
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

}