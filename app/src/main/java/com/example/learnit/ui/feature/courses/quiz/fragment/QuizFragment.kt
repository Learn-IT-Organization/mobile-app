package com.example.learnit.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.learnit.R
import com.example.learnit.databinding.FragmentQuizBinding
import com.example.learnit.ui.feature.courses.quiz.QuizPagerAdapter

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private var currentFragmentIndex = 0
    private var courseId: Int = -1
    private var chapterId: Int = -1
    private var lessonId: Int = -1
    private var score: Int = 0

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
        Log.d(TAG, "QuizFragment: $courseId $chapterId $lessonId")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.isUserInputEnabled = false
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
            val nextFragmentPosition = currentFragmentPosition + 1
            viewPager.setCurrentItem(nextFragmentPosition, true)
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