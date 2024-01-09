package com.example.learnit.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.learnit.R
import com.example.learnit.databinding.FragmentQuizTrueFalseBinding
import com.example.learnit.ui.feature.courses.quiz.viewModel.TrueFalseQuizViewModel
import kotlinx.coroutines.launch

class TrueFalseQuizFragment : Fragment() {
    private val viewModel: TrueFalseQuizViewModel by viewModels()
    private lateinit var binding: FragmentQuizTrueFalseBinding

    companion object {
        val TAG: String = TrueFalseQuizFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizTrueFalseBinding.inflate(inflater, container, false)
        viewModel.loadQuestionsAnswers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()

        binding.trueButton.setOnClickListener {
            viewModel.setUserResponse(true)
            Log.d(TAG, "responsee: ${viewModel.getUserResponse()}")
            setButtonState(binding.trueButton, true)
            setButtonState(binding.falseButton, false)
        }

        binding.falseButton.setOnClickListener {
            viewModel.setUserResponse(false)
            Log.d(TAG, "responsee: ${viewModel.getUserResponse()}")
            setButtonState(binding.trueButton, false)
            setButtonState(binding.falseButton, true)
        }

//        binding.submit.setOnClickListener {
//            if (viewModel.isResponseSet()) {
//                viewModel.sendUserResponse(
//                    QuizResultData(
//                        uqr_question_id = viewModel.currentQuestion?.questionId!!,
//                        uqr_user_id = SharedPreferences.getUserId().toInt(),
//                        response = listOf(
//                            QuizResponseData(
//                                option_text = if (viewModel.getUserResponse() == true) "true" else "false",
//                                is_correct = if (viewModel.getUserResponse() == true) true else false
//                            )
//                        ),
//                        is_correct = 1,
//                        score = 1,
//                        response_time = Date()
//                    )
//                )
//                viewModel.resetUserResponse()
//                setButtonState(binding.trueButton, false)
//                setButtonState(binding.falseButton, false)
//            }
//        }

    }

    private fun setButtonState(button: Button, selected: Boolean) {
        button.isSelected = selected
        button.isEnabled = !selected
        val colorResId = if (selected) R.color.gray else R.color.forest_green
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), colorResId))
        val textSizeResId =
            if (selected) R.dimen.selected_button_text_size else R.dimen.unselected_button_text_size
        val textSize = resources.getDimension(textSizeResId)
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is TrueFalseQuizViewModel.QuestionAnswersPageState.Loading -> {
                            Log.d(TAG, "Loading questionsAnswers...")
                        }

                        is TrueFalseQuizViewModel.QuestionAnswersPageState.Success -> {
                            Log.d(TAG, "QuestionsAnswers loaded")
                            Log.d(TAG, "randomQuestion: ${viewModel.currentQuestion}")
                            binding.lessonNumber.text =
                                viewModel.currentQuestion?.questionId.toString()
                            binding.question.text = viewModel.currentQuestion?.questionText
                        }

                        is TrueFalseQuizViewModel.QuestionAnswersPageState.Failure -> {
                            Log.e(TAG, "Error loading QuestionsAnswers: ${state.throwable}")
                        }
                    }
                }
            }
        }
    }
}