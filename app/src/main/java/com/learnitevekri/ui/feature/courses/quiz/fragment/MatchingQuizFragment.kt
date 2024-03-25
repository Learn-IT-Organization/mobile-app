package com.learnitevekri.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.learnitevekri.R
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.courses.quiz.model.MatchingQuestionData
import com.learnitevekri.data.courses.quiz.model.QuizResponseData
import com.learnitevekri.databinding.FragmentQuizMatchingBinding
import com.learnitevekri.ui.feature.courses.quiz.QuizButtonClickListener
import com.learnitevekri.ui.feature.courses.quiz.viewModel.SharedQuizViewModel
import java.util.Date

class MatchingQuizFragment : BaseQuizFragment<MatchingQuestionData>(),
    QuizButtonClickListener {

    override lateinit var binding: FragmentQuizMatchingBinding
    override val viewModel: SharedQuizViewModel by activityViewModels()
    override val TAG: String = MatchingQuizFragment::class.java.simpleName

    private var leftSelectedButton: Button? = null
    private var rightSelectedButton: Button? = null

    private val allPairs = mutableListOf<Pair<String, String>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizMatchingBinding.inflate(inflater, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.option1ButtonLeft.setOnClickListener { selectLeftButton(binding.option1ButtonLeft) }
        binding.option2ButtonLeft.setOnClickListener { selectLeftButton(binding.option2ButtonLeft) }
        binding.option3ButtonLeft.setOnClickListener { selectLeftButton(binding.option3ButtonLeft) }
        binding.option4ButtonLeft.setOnClickListener { selectLeftButton(binding.option4ButtonLeft) }

        binding.option1ButtonRight.setOnClickListener { selectRightButton(binding.option1ButtonRight) }
        binding.option2ButtonRight.setOnClickListener { selectRightButton(binding.option2ButtonRight) }
        binding.option3ButtonRight.setOnClickListener { selectRightButton(binding.option3ButtonRight) }
        binding.option4ButtonRight.setOnClickListener { selectRightButton(binding.option4ButtonRight) }

    }

    private fun selectLeftButton(leftButton: Button) {
        if (rightSelectedButton == null) {
            leftSelectedButton?.isEnabled = true
            leftSelectedButton?.setBackgroundColor(resources.getColor(R.color.md_theme_secondary_mediumContrast))
            leftSelectedButton?.isSelected = false
        } else {
            binding.option1ButtonLeft.isEnabled = false
            binding.option2ButtonLeft.isEnabled = false
            binding.option3ButtonLeft.isEnabled = false
            binding.option4ButtonLeft.isEnabled = false
        }

        leftSelectedButton = leftButton
        leftButton.isSelected = true
        leftButton.setBackgroundColor(resources.getColor(R.color.md_theme_secondary_mediumContrast))
        leftButton.isEnabled = false
    }

    private fun selectRightButton(rightButton: Button) {
        if (leftSelectedButton == null) {
            showToast(getString(R.string.please_select_left_side_first))
            return
        }

        if (rightButton != rightSelectedButton) {
            rightSelectedButton?.isSelected = false
            rightSelectedButton?.isEnabled = true
            rightSelectedButton?.setBackgroundColor(resources.getColor(R.color.md_theme_secondary_mediumContrast))
        }

        rightSelectedButton = rightButton
        rightButton.isSelected = true
        rightButton.isEnabled = false
        rightButton.setBackgroundColor(resources.getColor(R.color.md_theme_secondary_mediumContrast))

        getPairs()

        if (allPairs.isNotEmpty()) {
            binding.option1ButtonLeft.isEnabled = true
            binding.option2ButtonLeft.isEnabled = true
            binding.option3ButtonLeft.isEnabled = true
            binding.option4ButtonLeft.isEnabled = true
        }
        getPairs()

        if (allPairs.size == currentQuestion?.answers?.size) {
            onNextButtonClicked()
        }
    }

    override fun onNextButtonClicked() {
        viewModel.sendUserResponse(
            QuizResponseData(
                uqrQuestionId = currentQuestion?.questionId ?: -1,
                uqrUserId = SharedPreferences.getUserId().toInt(),
                response = allPairs,
                responseTime = Date(),
                score = 0.0f
            )
        )
        viewModel.currentQuestionItemLiveData.postValue(
            (viewModel.currentQuestionItemLiveData.value ?: 0) + 1
        )
        Log.d(TAG, "currentItem: ${viewModel.currentQuestionItemLiveData.value}")
        QuizFragment.currentQuestionNumber.postValue(
            QuizFragment.currentQuestionNumber.value?.plus(1) ?: 0
        )
        Log.d(TAG, "currentQuestionNumber: ${QuizFragment.currentQuestionNumber.value}")
    }

    private fun getPairs() {
        leftSelectedButton?.let { leftButton ->
            rightSelectedButton?.let { rightButton ->
                if (leftButton.isSelected && rightButton.isSelected) {
                    val leftText = leftButton.text.toString()
                    val rightText = rightButton.text.toString()
                    val pair = leftText to rightText

                    if (!allPairs.contains(pair)) {
                        allPairs.add(pair)
                    }
                } else {
                    val leftText = leftButton.text.toString()
                    val rightText = rightButton.text.toString()
                    val pair = leftText to rightText

                    if (allPairs.contains(pair)) {
                        allPairs.remove(pair)
                    }
                }
            }
        }
        Log.d(TAG, "allPairs: $allPairs")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun updateUI() {
        val answers = currentQuestion?.answers

        if (answers != null) {
            binding.option1ButtonLeft.text = answers[0].textLeft
            binding.option1ButtonRight.text = answers[0].textRight
            binding.option2ButtonLeft.text = answers[1].textLeft
            binding.option2ButtonRight.text = answers[1].textRight
            binding.option3ButtonLeft.text = answers[2].textLeft
            binding.option3ButtonRight.text = answers[2].textRight
            if (answers.size == 3) {
                binding.option4ButtonLeft.visibility = View.GONE
                binding.option4ButtonRight.visibility = View.GONE
                return
            } else {
                binding.option4ButtonLeft.text = answers[3].textLeft
                binding.option4ButtonRight.text = answers[3].textRight
            }
        }
    }
}