package com.example.learnit.ui.feature.courses.quiz.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.learnit.R
import com.example.learnit.databinding.FragmentQuizBinding
import com.example.learnit.ui.feature.courses.quiz.QuizPagerAdapter
import com.example.learnit.ui.feature.courses.quiz.model.QuestionsAnswersModel
import com.example.learnit.ui.feature.courses.quiz.viewModel.SharedQuizViewModel
import kotlinx.coroutines.launch

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private lateinit var viewPager: ViewPager2

    private val viewModel: SharedQuizViewModel by viewModels()

    private var courseId: Int = -1
    private var chapterId: Int = -1
    private var lessonId: Int = -1

    private var numberOfQuestions: Int = 0
    private var totalScore: Float = 0.0f

    private var questionsAnswers: List<QuestionsAnswersModel> = emptyList()
    private var currentFragment: Fragment? = null
    private var currentQuestionId = 0

    companion object {
        val TAG: String = QuizFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false)

        courseId = arguments?.getInt("courseId", -1) ?: -1
        chapterId = arguments?.getInt("chapterId", -1) ?: -1
        lessonId = arguments?.getInt("lessonId", -1) ?: -1
        numberOfQuestions = viewModel.numberOfQuestions

        viewPager = binding.viewPager
        viewModel.loadAllQuestionsAnswers(courseId, chapterId, lessonId)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()

        binding.escapeButton.setOnClickListener {
            showExitConfirmationDialog()
        }

        binding.bookButton.setOnClickListener {
            findNavController().navigate(R.id.action_quizFragment_to_TheoryFragment)
        }

        binding.checkAndSubmitButton.setOnClickListener {
            onCheckAndSubmitButtonClicked()
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is SharedQuizViewModel.QuestionAnswersPageState.Loading -> {
                            Log.d(TAG, "Loading questionsAnswers...")
                        }

                        is SharedQuizViewModel.QuestionAnswersPageState.Success -> {
                            Log.d(TAG, "QuestionsAnswers loaded")
                            questionsAnswers = state.questionsAnswersData
                            setUpAdapter()
                        }

                        is SharedQuizViewModel.QuestionAnswersPageState.Failure -> {
                            Log.e(
                                TAG,
                                "Error loading MultipleChoice QuestionsAnswers: ${state.throwable}"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext()).setTitle("Leave Quiz")
            .setMessage("Are you sure you want to leave the quiz? Your progress will be lost.")
            .setPositiveButton("Yes") { _, _ ->
                activity?.onBackPressed()
            }.setNegativeButton("No", null).show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateScoreUI(score: Float) {
        binding.textScore.text = "Score: ${totalScore + score}"
        totalScore += score
        Log.d(TAG, "totalScore: $totalScore")
    }

    private fun showQuizResultDialog() {
        AlertDialog.Builder(requireContext()).setTitle("Quiz Result")
            .setMessage("Your score is: $totalScore").setPositiveButton("OK") { _, _ ->
                activity?.onBackPressed()
            }.show()
    }

    private fun setUpAdapter() {
        viewPager.isUserInputEnabled = false
        viewPager.setCurrentItem(0, false)

        val adapter = QuizPagerAdapter(
            requireActivity(),
            questionList = questionsAnswers,
            courseId = courseId,
            chapterId = chapterId,
            lessonId = lessonId
        )
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentFragmentPosition = viewPager.currentItem
                currentQuestionId = viewPager.adapter?.getItemId(currentFragmentPosition)?.toInt()!!
                Log.d(TAG, "currentQuestionId: $currentQuestionId")
                currentFragment = childFragmentManager.findFragmentByTag("quizFragment$currentQuestionId")
                Log.d(TAG, "currentQuestionId: $currentQuestionId, currentFragment: $currentFragment")
            }
        })
    }

    private fun onCheckAndSubmitButtonClicked() {
        val currentFragmentPosition = viewPager.currentItem
        val nextFragmentPosition = currentFragmentPosition + 1

        Log.d(TAG, "currentFragment: $currentFragment")

        if (currentFragment is MultipleChoiceQuizFragment) {
            (currentFragment as MultipleChoiceQuizFragment).onNextButtonClicked()
        } else if (currentFragment is TrueFalseQuizFragment) {
            (currentFragment as TrueFalseQuizFragment).onNextButtonClicked()
        }

        viewPager.setCurrentItem(
            nextFragmentPosition, true
        )

        if (currentFragmentPosition >= 9) {
            showQuizResultDialog()
        }
    }

}