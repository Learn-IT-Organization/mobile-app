package com.example.learnit.ui.feature.courses.quiz.fragment

import QuizResponseModel
import UserResponseModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.learnit.data.SharedPreferences
import com.example.learnit.databinding.FragmentQuizMultipleChoiceBinding
import com.example.learnit.ui.feature.courses.quiz.QuizButtonClickListener
import com.example.learnit.ui.feature.courses.quiz.model.QuestionsAnswersModel
import com.example.learnit.ui.feature.courses.quiz.viewModel.SharedQuizViewModel
import java.util.Date

class MultipleChoiceQuizFragment : BaseQuizFragment(), QuizButtonClickListener {
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
        lessonId = arguments?.getInt("lessonId", -1) ?: -1

        if (arguments != null) {
            currentQuestion =
                requireArguments().getSerializable("question") as QuestionsAnswersModel?
            Log.d(TAG, "question: ${currentQuestion?.questionText}")
            updateUI()
        }

        return binding.root
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

    private fun updateUI() {
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
}