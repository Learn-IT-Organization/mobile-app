package com.learnitevekri.ui.feature.courses.quiz.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.learnitevekri.R
import com.learnitevekri.databinding.FragmentAddQuestionBinding
import com.learnitevekri.ui.feature.courses.quiz.adapter.AddQuestionPagerAdapter
import com.learnitevekri.ui.feature.courses.quiz.viewModel.SharedQuizViewModel

class AddQuestionFragment : Fragment() {
    private lateinit var binding: FragmentAddQuestionBinding
    private val viewModel: SharedQuizViewModel by activityViewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var questionTypeSpinner: Spinner
    private lateinit var adapter: AddQuestionPagerAdapter
    private var lessonId: Int = -1
    private var chapterId = -1
    private var courseId = -1

    private val TAG = AddQuestionFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddQuestionBinding.inflate(inflater, container, false)

        courseId  = arguments?.getInt("course_id", -1) ?: -1
        chapterId = arguments?.getInt("chapter_id", -1) ?: -1
        lessonId = arguments?.getInt("lesson_id", -1) ?: -1

        Log.d(TAG, "Course ID, Chapter ID, Lesson ID: $courseId, $chapterId, $lessonId")

        viewPager = binding.viewPager
        questionTypeSpinner = binding.questionTypeSpinner

        binding.escapeButton.setOnClickListener {

        }

        questionTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                updateAdapter(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        viewModel.questionCounterLiveData.observe(viewLifecycleOwner) { counter ->
            binding.questionCount.text = "$counter/10"
            binding.progressBar.progress = counter
            if (counter >= 11) {
                showQuizCompletedDialog()
            } else {
                resetViewPager()
            }
        }

        return binding.root
    }

    private fun updateAdapter(selectedPosition: Int) {
        adapter = AddQuestionPagerAdapter(requireActivity(), selectedPosition, lessonId, chapterId, courseId)
        viewPager.adapter = adapter
    }

    private fun resetViewPager() {
        viewPager.adapter = null
        updateAdapter(questionTypeSpinner.selectedItemPosition)
    }

    private fun showQuizCompletedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Quiz uploading Completed")
            .setMessage("You have completed the quiz upload.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                findNavController().navigate(R.id.action_addQuestionFragment_to_coursesFragment)
            }
            .setCancelable(false)
            .show()
    }
}