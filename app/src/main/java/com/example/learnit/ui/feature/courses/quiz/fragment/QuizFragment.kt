package com.example.learnit.ui.feature.courses.quiz.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.learnit.R
import com.example.learnit.data.courses.quiz.repository.QuizResultRepositoryImpl
import com.example.learnit.databinding.FragmentQuizBinding
import com.example.learnit.ui.feature.courses.quiz.QuizPagerAdapter

class QuizFragment : Fragment(), QuizPagerAdapter.QuizButtonClickListener {

    private lateinit var binding: FragmentQuizBinding
    private var currentFragmentIndex = 0

    private var courseId: Int = -1
    private var chapterId: Int = -1
    private var lessonId: Int = -1
    private var initialScore: Int = 0

    companion object {
        val TAG: String = QuizFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizBinding.inflate(inflater, container, false)

        courseId = arguments?.getInt("courseId", -1) ?: -1
        chapterId = arguments?.getInt("chapterId", -1) ?: -1
        lessonId = arguments?.getInt("lessonId", -1) ?: -1

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager: ViewPager2 = binding.viewPager
        val pagerAdapter = QuizPagerAdapter(requireActivity(), 7, courseId, chapterId, lessonId)
        viewPager.adapter = pagerAdapter
        currentFragmentIndex++

        binding.escapeButton.setOnClickListener {
            showExitConfirmationDialog()
        }
        binding.bookButton.setOnClickListener {
            findNavController().navigate(R.id.action_quizFragment_to_TheoryFragment)
        }
        binding.checkAndSubmitButton.setOnClickListener {
            onNextButtonClicked()
        }

        observeScore()
    }

    @SuppressLint("SetTextI18n")
    private fun observeScore() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            QuizResultRepositoryImpl.latestScore.collect { score ->
                Log.d(TAG, "Observed Score: $score")
                binding.textScore.text = "Score: ${initialScore + score}"
            }
        }
    }

    override fun onNextButtonClicked() {
        val currentFragment =
            requireActivity().supportFragmentManager.fragments[currentFragmentIndex]

        if (currentFragment is QuizPagerAdapter.QuizButtonClickListener) {
            Log.d(TAG, "onNextButtonClicked: $currentFragment")

            if (currentFragmentIndex % 2 == 0) {
                if (currentFragment is MultipleChoiceQuizFragment) {
                    currentFragment.onNextButtonClicked()
                }
            } else {
                if (currentFragment is TrueFalseQuizFragment) {
                    currentFragment.onNextButtonClicked()
                }
            }

            currentFragmentIndex++
            Log.d(TAG, "currentFragmentIndex: $currentFragmentIndex")
            if (currentFragmentIndex <= 7) {
                binding.viewPager.setCurrentItem(currentFragmentIndex, true)
            } else {
                Log.d(TAG, "Quiz finished")
            }
        }
    }


    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Leave Quiz")
            .setMessage("Are you sure you want to leave the quiz? Your progress will be lost.")
            .setPositiveButton("Yes") { _, _ ->
                activity?.onBackPressed()
            }
            .setNegativeButton("No", null)
            .show()
    }
}