package com.example.learnit.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.learnit.data.courses.quiz.model.SortingQuestionData
import com.example.learnit.databinding.FragmentQuizSortingBinding
import com.example.learnit.ui.feature.courses.quiz.QuizButtonClickListener
import com.example.learnit.ui.feature.courses.quiz.viewModel.SharedQuizViewModel

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
        return super.onCreateView(inflater, container, savedInstanceState)
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
        val  currentQuestion = currentQuestion?.answers?.firstOrNull()
        binding.upText.text = currentQuestion?.ansUpText
        binding.downText.text = currentQuestion?.ansDownText
    }
}