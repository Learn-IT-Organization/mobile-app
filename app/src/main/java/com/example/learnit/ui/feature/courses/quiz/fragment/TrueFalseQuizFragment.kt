package com.example.learnit.ui.feature.courses.quiz.fragment

import QuizResponseModel
import UserResponseModel
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
import com.example.learnit.data.SharedPreferences
import com.example.learnit.data.courses.quiz.mapper.mapToUserResponseData
import com.example.learnit.databinding.FragmentQuizTrueFalseBinding
import com.example.learnit.ui.feature.courses.quiz.QuizPagerAdapter
import com.example.learnit.ui.feature.courses.quiz.model.QuestionsAnswersModel
import com.example.learnit.ui.feature.courses.quiz.viewModel.SharedQuizViewModel
import kotlinx.coroutines.launch
import java.util.Date

class TrueFalseQuizFragment : BaseQuizFragment(), QuizPagerAdapter.QuizButtonClickListener {
    override lateinit var binding: FragmentQuizTrueFalseBinding

    override val viewModel: SharedQuizViewModel by viewModels()
    override val TAG: String = TrueFalseQuizFragment::class.java.simpleName
    private var currentQuestion: QuestionsAnswersModel? = null

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
                            currentQuestion = viewModel.shuffleAndSelectQuestion("true_false")
                            binding.question.text = currentQuestion?.questionText
                        }

                        is SharedQuizViewModel.QuestionAnswersPageState.Failure -> {
                            Log.e(
                                TAG,
                                "Error loading TrueFalse QuestionsAnswers: ${state.throwable}"
                            )
                        }
                    }
                }
            }
        }
    }

    override  fun onNextButtonClicked() {
        viewModel.sendUserResponse(
            UserResponseModel(
                uqrQuestionId = currentQuestion?.questionId ?: -1,
                uqrUserId = SharedPreferences.getUserId().toInt(),
                response = QuizResponseModel(listOf(viewModel.getUserResponse())),
                responseTime = Date(),
                score = 0.0f
            ).mapToUserResponseData()
        )
        Log.d(TAG, "question id:${currentQuestion?.questionId}")
        Log.d(TAG, "user response: ${listOf(viewModel.getUserResponse())}")
        Log.d(TAG, "true_false onNextButtonClicked")
    }
}