package com.example.learnit.ui.feature.courses.quiz.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import com.example.learnit.R
import com.example.learnit.data.SharedPreferences
import com.example.learnit.data.courses.quiz.model.QuizResponseData
import com.example.learnit.data.courses.quiz.model.TrueFalseQuestionData
import com.example.learnit.databinding.FragmentQuizTrueFalseBinding
import com.example.learnit.ui.feature.courses.quiz.QuizButtonClickListener
import com.example.learnit.ui.feature.courses.quiz.viewModel.SharedQuizViewModel
import java.util.Date

class TrueFalseQuizFragment : BaseQuizFragment<TrueFalseQuestionData>(), QuizButtonClickListener {
    override lateinit var binding: FragmentQuizTrueFalseBinding
    override val viewModel: SharedQuizViewModel by activityViewModels()
    override val TAG: String = TrueFalseQuizFragment::class.java.simpleName
    private var hasAnswered = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizTrueFalseBinding.inflate(inflater, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trueButton.setOnClickListener {
            viewModel.setUserResponse(true)
            setButtonState(binding.trueButton, true)
            setButtonState(binding.falseButton, false)
            hasAnswered = true
            updateButtonStyle()
        }

        binding.falseButton.setOnClickListener {
            viewModel.setUserResponse(false)
            setButtonState(binding.trueButton, false)
            setButtonState(binding.falseButton, true)
            hasAnswered = true
            updateButtonStyle()
        }

        binding.verifyButton.setOnClickListener {
            if (hasAnswered) {
                onNextButtonClicked()
            } else {
                animateButtonSize(binding.trueButton)
                animateButtonSize(binding.falseButton)
            }
        }
    }

    private fun updateButtonStyle() {
        binding.verifyButton.setBackgroundColor(resources.getColor(R.color.md_theme_secondary_mediumContrast))
        binding.verifyButton.setTextColor(resources.getColor(R.color.md_theme_onPrimary))
    }

    private fun animateButtonSize(button: ImageView) {
        val scale = 1.3f

        val scaleX = ObjectAnimator.ofFloat(button, "scaleX", scale, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(button, "scaleY", scale, 1.0f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.duration = 800
        animatorSet.start()
    }

    private fun setButtonState(button: ImageView, selected: Boolean) {
        button.isSelected = selected
        button.isEnabled = !selected

        val scale = if (selected) 1.3f else 1.0f

        val scaleX = ObjectAnimator.ofFloat(button, "scaleX", scale)
        val scaleY = ObjectAnimator.ofFloat(button, "scaleY", scale)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.duration = 200
        animatorSet.start()
    }


    override fun onNextButtonClicked() {
        viewModel.sendUserResponse(
            QuizResponseData(
                uqrQuestionId = currentQuestion?.questionId ?: -1,
                uqrUserId = SharedPreferences.getUserId().toInt(),
                response = listOf(viewModel.getUserResponse()),
                responseTime = Date(),
                score = 0.0f
            )
        )
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

    override fun updateUI() {
        binding.question.text = currentQuestion?.questionText
        val userResponse = viewModel.getUserResponse()
        binding.verifyButton.isEnabled = userResponse != null
    }

}