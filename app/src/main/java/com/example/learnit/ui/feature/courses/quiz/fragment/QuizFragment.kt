package com.example.learnit.ui.feature.courses.quiz.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.learnit.R
import com.example.learnit.data.courses.quiz.model.BaseQuestionData
import com.example.learnit.databinding.FragmentQuizBinding
import com.example.learnit.ui.feature.courses.quiz.QuizPagerAdapter
import com.example.learnit.ui.feature.courses.quiz.viewModel.SharedQuizViewModel
import kotlinx.coroutines.launch

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding

    private val viewModel: SharedQuizViewModel by activityViewModels()

    private var courseId: Int = -1
    private var chapterId: Int = -1
    private var lessonId: Int = -1

    private var numberOfQuestions: Int = 0
    private var totalScore: Float = 0.0f

    private var questionsAnswers: List<BaseQuestionData> = emptyList()

    companion object {
        lateinit var viewPager: ViewPager2
        val TAG: String = QuizFragment::class.java.simpleName
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(inflater, container, false)

        courseId = arguments?.getInt("courseId", -1) ?: -1
        chapterId = arguments?.getInt("chapterId", -1) ?: -1
        lessonId = arguments?.getInt("lessonId", -1) ?: -1

        viewPager = binding.viewPager
        viewModel.loadAllQuestionsAnswers(courseId, chapterId, lessonId)

        if (viewPager.currentItem == numberOfQuestions - 1) {
            showQuizResultDialog()
        }

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

        observeScore()
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
                            numberOfQuestions = viewModel.numberOfQuestions
                            Log.d(TAG, "numberOfQuestions: $numberOfQuestions")
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

        viewPager.setCurrentItem(
            0, true
        )
    }

    private fun observeScore() {
        Log.d(TAG, "Observing score... + ${viewModel.scoreLiveData.hashCode()}")
        val observer = Observer<Float> { totalScore ->
            Log.d(TAG, "Score updated: $totalScore")
            updateScoreUI(totalScore)
            if (viewPager.currentItem == numberOfQuestions - 1) {
                showQuizResultDialog()
            }
        }
        viewModel.scoreLiveData.observe(requireActivity(), observer)
    }

    @SuppressLint("SetTextI18n")
    fun updateScoreUI(newScore: Float) {
        totalScore += newScore * 10
        binding.textScore.text = "Score: ${totalScore.toInt()}"
        Log.d(TAG, "totalScore: $totalScore")
    }

}