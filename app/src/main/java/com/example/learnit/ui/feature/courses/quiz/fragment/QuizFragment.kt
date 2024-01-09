package com.example.learnit.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.learnit.R
import com.example.learnit.databinding.FragmentQuizBinding
import com.example.learnit.ui.feature.cou.MultipleChoiceQuizFragment

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private var currentFragmentIndex = 0
    private val quizTypes = listOf("multiple_choice", "true_false")
    private val random = java.util.Random()
    private var courseId: Int = -1
    private var chapterId: Int = -1
    private var lessonId: Int = -1

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

        Log.d(TAG, "adatok: $courseId $chapterId $lessonId")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showQuizFragment()
        binding.escapeButton.setOnClickListener {
            showExitConfirmationDialog()
        }
        binding.bookButton.setOnClickListener {
            findNavController().navigate(R.id.action_quizFragment_to_TheoryFragment)
        }
        binding.checkAndSubmitButton.setOnClickListener {
            onNextButtonClicked()
        }
    }

    private fun showQuizFragment() {
        val fragment = when (val randomQuizType = quizTypes[random.nextInt(quizTypes.size)]) {
            "multiple_choice"  -> {
                val multipleChoiceFragment = MultipleChoiceQuizFragment()
                multipleChoiceFragment.setQuizData(courseId, chapterId, lessonId)
                multipleChoiceFragment
            }
            "true_false" -> {
                val trueFalseFragment = TrueFalseQuizFragment()
                trueFalseFragment.setQuizData(courseId, chapterId, lessonId)
                trueFalseFragment
            }
            else -> throw IllegalArgumentException("Invalid quiz type: $randomQuizType")
        }

        childFragmentManager.beginTransaction()
            .replace(R.id.frameQuizContainer, fragment)
            .commit()
    }

    private fun onNextButtonClicked() {
        currentFragmentIndex++
        if (currentFragmentIndex < 10) {
            showQuizFragment()
        } else {
            Log.d(TAG, "Quiz finished")
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