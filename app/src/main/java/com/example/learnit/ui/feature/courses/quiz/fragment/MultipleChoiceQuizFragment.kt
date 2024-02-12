package com.example.learnit.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.learnit.data.SharedPreferences
import com.example.learnit.data.courses.quiz.model.MultipleChoiceQuestionData
import com.example.learnit.data.courses.quiz.model.QuizResponseData
import com.example.learnit.databinding.FragmentQuizMultipleChoiceBinding
import com.example.learnit.ui.feature.courses.quiz.QuizButtonClickListener
import com.example.learnit.ui.feature.courses.quiz.viewModel.SharedQuizViewModel
import java.util.Date

class MultipleChoiceQuizFragment : BaseQuizFragment<MultipleChoiceQuestionData>(),
    QuizButtonClickListener {
    override lateinit var binding: FragmentQuizMultipleChoiceBinding
    override val viewModel: SharedQuizViewModel by activityViewModels()
    override val TAG: String = MultipleChoiceQuizFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizMultipleChoiceBinding.inflate(inflater, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkAndSubmitButton.setOnClickListener {
            onNextButtonClicked()
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
        viewModel.sendUserResponse(
            QuizResponseData(
                uqrQuestionId = currentQuestion?.questionId ?: -1,
                uqrUserId = SharedPreferences.getUserId().toInt(),
                response = getSelectedAnswers(),
                responseTime = Date(),
                score = 0.0f
            )
        )
        Log.d(TAG, "question id:${currentQuestion?.questionId}")
        Log.d(TAG, "multiple_choice onNextButtonClicked")
        clearCheckBoxes()
        QuizFragment.viewPager.currentItem += 1
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

    override fun updateUI() {
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