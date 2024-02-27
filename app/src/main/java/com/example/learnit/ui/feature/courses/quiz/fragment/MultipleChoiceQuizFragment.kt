package com.example.learnit.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.learnit.R
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

    private val selectedButtons = mutableListOf<Button>()

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

        binding.verifyButton.isEnabled = false

        val optionButtons = listOf(
            binding.option1Button,
            binding.option2Button,
            binding.option3Button,
            binding.option4Button
        )

        optionButtons.forEach { button ->
            button.setOnClickListener {
                button.isSelected = !button.isSelected
                updateButtonAppearance(button)
            }
        }

        binding.verifyButton.setOnClickListener {
            if (hasAtLeastOneAnswerSelected()) {
                onNextButtonClicked()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_select_an_answer),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateButtonAppearance(button: Button) {
        if (button.isSelected) {
            button.setBackgroundColor(resources.getColor(R.color.md_theme_inversePrimary_mediumContrast))
            binding.verifyButton.isEnabled = true
            updateVerifyButtonStyle(true)
            selectedButtons.add(button)
        } else {
            button.setBackgroundColor(resources.getColor(R.color.md_theme_secondary_mediumContrast))
            selectedButtons.remove(button)
            if (selectedButtons.isEmpty()) {
                binding.verifyButton.isEnabled = false
                updateVerifyButtonStyle(false)
            }
        }
    }

    private fun hasAtLeastOneAnswerSelected(): Boolean {
        return selectedButtons.isNotEmpty()
    }

    private fun clearButtons() {
        val optionButtons = listOf(
            binding.option1Button,
            binding.option2Button,
            binding.option3Button,
            binding.option4Button
        )

        optionButtons.forEach {
            it.isSelected = false
            updateButtonAppearance(it)
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
        clearButtons()
        viewModel.currentQuestionItemLiveData.postValue(
            (viewModel.currentQuestionItemLiveData.value ?: 0) + 1
        )
        Log.d(TAG, "currentItem: ${viewModel.currentQuestionItemLiveData.value}")
        QuizFragment.currentQuestionNumber.postValue(
            QuizFragment.currentQuestionNumber.value?.plus(
                1
            ) ?: 0
        )
        Log.d(TAG, "currentQuestionNumber: ${QuizFragment.currentQuestionNumber.value}")
    }

    private fun getSelectedAnswers(): List<Boolean> {
        val optionButtons = listOf(
            binding.option1Button,
            binding.option2Button,
            binding.option3Button,
            binding.option4Button
        )

        val selectedIndices = optionButtons.indices.filter { optionButtons[it].isSelected }

        val sortedSelectedIndices = selectedIndices.sorted()

        return List(4) { index -> sortedSelectedIndices.contains(index) }
    }

    private fun updateVerifyButtonStyle(isEnabled: Boolean) {
        if (isEnabled) {
            binding.verifyButton.setBackgroundColor(resources.getColor(R.color.md_theme_secondary_mediumContrast))
            binding.verifyButton.setTextColor(resources.getColor(R.color.md_theme_onPrimary))
        } else {
           binding.verifyButton.setBackgroundColor(resources.getColor(R.color.md_theme_outlineVariant_mediumContrast))
            binding.verifyButton.setTextColor(resources.getColor(R.color.md_theme_outline_mediumContrast))
        }
    }

    override fun updateUI() {
        binding.questionTextView.text = currentQuestion?.questionText
        val answers = currentQuestion?.answers

        if (answers != null) {
            if (answers.isNotEmpty()) {
                val optionButtons = listOf(
                    binding.option1Button,
                    binding.option2Button,
                    binding.option3Button,
                    binding.option4Button
                )

                for ((index, answer) in answers.withIndex()) {
                    optionButtons[index].text = answer
                    updateButtonAppearance(optionButtons[index])
                }
            }
        }
    }
}