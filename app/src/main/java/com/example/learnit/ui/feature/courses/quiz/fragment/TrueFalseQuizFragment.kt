package com.example.learnit.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.learnit.R
import com.example.learnit.databinding.FragmentQuizTrueFalseBinding
import com.example.learnit.ui.feature.courses.quiz.viewModel.SharedQuizViewModel
import kotlinx.coroutines.launch

class TrueFalseQuizFragment : BaseQuizFragment() {
    override val viewModel: SharedQuizViewModel by viewModels()
    override lateinit var binding: FragmentQuizTrueFalseBinding
    override val TAG: String = TrueFalseQuizFragment::class.java.simpleName

    private var courseId: Int = -1
    private var chapterId: Int = -1
    private var lessonId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizTrueFalseBinding.inflate(inflater, container, false)
        courseId = arguments?.getInt("courseId", -1) ?: -1
        chapterId = arguments?.getInt("chapterId", -1) ?: -1
        lessonId = arguments?.getInt("lessonId", -1) ?: -1
        viewModel.loadAllQuestionsAnswers(courseId, chapterId, lessonId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()

        binding.trueButton.setOnClickListener {
            viewModel.setUserResponse(true)
            Log.d(TAG, "response: ${viewModel.getUserResponse()}")
            setButtonState(binding.trueButton, true)
            setButtonState(binding.falseButton, false)
        }

        binding.falseButton.setOnClickListener {
            viewModel.setUserResponse(false)
            Log.d(TAG, "response: ${viewModel.getUserResponse()}")
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
        val textSizeResId =
            if (selected) R.dimen.selected_button_text_size else R.dimen.unselected_button_text_size
        val textSize = resources.getDimension(textSizeResId)
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    override fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is SharedQuizViewModel.QuestionAnswersPageState.Loading -> {
                            Log.d(TAG, "Loading TrueFalse questionsAnswers...")
                        }

                        is SharedQuizViewModel.QuestionAnswersPageState.Success -> {
                            Log.d(TAG, "TrueFalse QuestionsAnswers loaded")
                            val currentQuestion = viewModel.shuffleAndSelectQuestion("true_false")
                            binding.question.text = currentQuestion?.questionText
                        }

                        is SharedQuizViewModel.QuestionAnswersPageState.Failure -> {
                            Log.e(TAG, "Error loading TrueFalse QuestionsAnswers: ${state.throwable}")
                        }
                    }
                }
            }
        }
    }
}