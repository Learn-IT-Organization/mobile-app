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
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.learnit.R
import com.example.learnit.databinding.FragmentQuizBinding
import com.example.learnit.ui.feature.courses.quiz.QuizPagerAdapter
import com.example.learnit.ui.feature.courses.quiz.viewModel.SharedQuizViewModel

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private val viewModel: SharedQuizViewModel by viewModels()

    private var courseId: Int = -1
    private var chapterId: Int = -1
    private var lessonId: Int = -1
    private var numberOfQuestions: Int = 0
    private var totalScore: Float = 0.0f

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
        Log.d(TAG, "QuizFragment: $courseId $chapterId $lessonId")

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.isUserInputEnabled = false
        viewPager.setCurrentItem(0, false)
        val verifyButton = binding.checkAndSubmitButton
        viewPager.adapter = QuizPagerAdapter(
            requireActivity(),
            numberOfQuestions = 10,
            courseId = courseId,
            chapterId = chapterId,
            lessonId = lessonId
        )

        binding.escapeButton.setOnClickListener {
            showExitConfirmationDialog()
        }

        binding.bookButton.setOnClickListener {
            findNavController().navigate(R.id.action_quizFragment_to_TheoryFragment)
        }

        verifyButton.setOnClickListener {

            val currentFragmentPosition = viewPager.currentItem
            Log.d(TAG, "currentFragmentPosition: $currentFragmentPosition")
            val nextFragmentPosition = currentFragmentPosition + 1
            val currentFragment =
                requireActivity().supportFragmentManager.fragments.getOrNull(nextFragmentPosition)
            //val currentFragment = requireActivity().supportFragmentManager.findFragmentByTag("MultipleChoiceQuizFragment")
            viewPager.setCurrentItem(
                nextFragmentPosition, true
            )
            Log.d(TAG, "currentFragment: $currentFragment")
            Log.d(TAG, "nextFragmentPosition: $nextFragmentPosition")
            if (currentFragment is MultipleChoiceQuizFragment) {
                Log.d(TAG, "1 currentFragment: $currentFragment")
                currentFragment.onNextButtonClicked()
            } else if (currentFragment is TrueFalseQuizFragment) {
                Log.d(TAG, "2 currentFragment: $currentFragment")
                currentFragment.onNextButtonClicked()
            }

            if (currentFragmentPosition >= 9) {
                showQuizResultDialog()
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

}