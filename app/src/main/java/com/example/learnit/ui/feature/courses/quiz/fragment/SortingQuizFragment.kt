package com.example.learnit.ui.feature.courses.quiz.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.learnit.R
import com.example.learnit.data.SharedPreferences
import com.example.learnit.data.courses.quiz.model.QuizResponseData
import com.example.learnit.data.courses.quiz.model.SortingQuestionData
import com.example.learnit.databinding.FragmentQuizSortingBinding
import com.example.learnit.ui.feature.courses.quiz.QuizButtonClickListener
import com.example.learnit.ui.feature.courses.quiz.viewModel.SharedQuizViewModel
import java.util.Date

class SortingQuizFragment : BaseQuizFragment<SortingQuestionData>(), QuizButtonClickListener {
    override lateinit var binding: FragmentQuizSortingBinding
    override val viewModel: SharedQuizViewModel by activityViewModels()
    override val TAG: String = SortingQuizFragment::class.java.simpleName
    private var executedClicks = 0
    private val upResponses = mutableListOf<String>()
    private val downResponses = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizSortingBinding.inflate(inflater, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.upButton.setOnClickListener {
            buttonClicked(it)
        }
        binding.downButton.setOnClickListener {
            buttonClicked(it)
        }
    }

    private fun buttonClicked(view: View) {
        val concepts = currentQuestion?.answers?.firstOrNull()?.concepts

        if (!concepts.isNullOrEmpty()) {
            val currentIndex = concepts.indexOf(binding.concept.text.toString())
            val fadeOut = ObjectAnimator.ofFloat(binding.concept, "alpha", 1f, 0f)
            fadeOut.duration = 500

            fadeOut.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    val nextIndex = (currentIndex + 1) % concepts.size
                    binding.concept.text = concepts[nextIndex]
                    val fadeIn = ObjectAnimator.ofFloat(binding.concept, "alpha", 0f, 1f)
                    fadeIn.duration = 500
                    fadeIn.start()
                    executedClicks++

                    when (view.id) {
                        R.id.upButton -> upResponses.add(concepts[currentIndex])
                        R.id.downButton -> downResponses.add(concepts[currentIndex])
                    }
                    Log.d(TAG, "responses: $upResponses $downResponses")

                    if (executedClicks == concepts.size) {
                        executedClicks = 0
                        onNextButtonClicked()
                    }
                }
            })
            fadeOut.start()
        }
    }

    override fun onNextButtonClicked() {
        viewModel.sendUserResponse(
            QuizResponseData(
                uqrQuestionId = currentQuestion?.questionId ?: -1,
                uqrUserId = SharedPreferences.getUserId().toInt(),
                response = mapOf(
                    "up" to upResponses,
                    "down" to downResponses
                ),
                responseTime = Date(),
                score = 0.0f
            )
        )
        Log.d(TAG, "question id:${currentQuestion?.questionId}")
        Log.d(TAG, "user response: ${listOf(viewModel.getUserResponse())}")
        Log.d(TAG, "sorting onNextButtonClicked")
        QuizFragment.viewPager.currentItem += 1
    }

    override fun updateUI() {
        val currentQuestion = currentQuestion?.answers?.firstOrNull()
        binding.upButton.text = currentQuestion?.ansUpText
        binding.downButton.text = currentQuestion?.ansDownText
        binding.concept.text = currentQuestion?.concepts?.get(0)
    }
}