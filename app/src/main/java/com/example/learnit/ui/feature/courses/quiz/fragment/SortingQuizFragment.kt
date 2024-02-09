package com.example.learnit.ui.feature.courses.quiz.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.learnit.R
import com.example.learnit.data.SharedPreferences
import com.example.learnit.data.courses.quiz.model.MultipleChoiceQuestionData
import com.example.learnit.data.courses.quiz.model.SortingQuestionData
import com.example.learnit.data.courses.quiz.model.UserResponseData
import com.example.learnit.databinding.FragmentQuizMultipleChoiceBinding
import com.example.learnit.databinding.FragmentQuizSortingBinding
import com.example.learnit.databinding.FragmentQuizTrueFalseBinding
import com.example.learnit.ui.feature.courses.quiz.QuizButtonClickListener
import com.example.learnit.ui.feature.courses.quiz.viewModel.SharedQuizViewModel
import java.util.Date

class SortingQuizFragment : BaseQuizFragment<SortingQuestionData>(), QuizButtonClickListener {
    override lateinit var binding: FragmentQuizSortingBinding
    override val viewModel: SharedQuizViewModel by activityViewModels()
    override val TAG: String = SortingQuizFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizSortingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkAndSubmitButton.setOnClickListener {
            onNextButtonClicked()
        }

    }

    override fun onNextButtonClicked() {

    }
    override fun updateUI() {
        binding.upText.text = currentQuestion?.answers?.ansUpText
        binding.downText.text = currentQuestion?.answers?.ansDownText
    }
}
