package com.example.learnit.ui.feature.courses.quiz.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.learnit.databinding.FragmentQuizSortingBinding
import com.example.learnit.ui.feature.courses.quiz.QuizButtonClickListener
import com.example.learnit.ui.feature.courses.quiz.QuizPagerAdapter
import com.example.learnit.ui.feature.courses.quiz.viewModel.SortingQuizViewModel

class SortingQuizFragment : Fragment(), QuizButtonClickListener {
    private lateinit var binding: FragmentQuizSortingBinding
    private var initialX: Float = 0F
    private var initialY: Float = 0F

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizSortingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.concept.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = event.x
                    initialY = event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.x - initialX
                    val deltaY = event.y - initialY

                    binding.concept.x += deltaX
                    binding.concept.y += deltaY

                    initialX = event.x
                    initialY = event.y
                }
            }
            true
        }
    }

    override  fun onNextButtonClicked() {
        TODO("Not yet implemented")
    }
}