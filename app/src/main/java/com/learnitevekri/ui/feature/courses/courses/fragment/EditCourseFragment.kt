package com.learnitevekri.ui.feature.courses.courses.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.learnitevekri.data.courses.course.model.EditCourseData
import com.learnitevekri.databinding.FragmentEditCourseBinding
import com.learnitevekri.ui.feature.courses.courses.viewModel.CoursesViewModel
import kotlinx.coroutines.launch

class EditCourseFragment : Fragment() {
    private val viewModel: CoursesViewModel by viewModels()
    private lateinit var binding: FragmentEditCourseBinding

    companion object {
        val TAG: String = EditCourseFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val courseId = arguments?.getInt("courseId")
        if (courseId != null) {
            viewModel.loadCourseById(courseId)
        }

        observeState()
        setupCharacterCount()

        binding.btnSave.setOnClickListener {
            val updatedCourse = EditCourseData(
                courseName = binding.etCourseName.text.toString(),
                programmingLanguage = binding.etProgrammingLanguage.text.toString(),
                courseDescription = binding.etCourseDescription.text.toString()
            )
            if (courseId != null) {
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        viewModel.editCourse(courseId, updatedCourse)
                        Snackbar.make(
                            requireView(), "Course updated successfully", Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    } catch (e: Exception) {
                        Snackbar.make(
                            requireView(), "Failed to update course", Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state2.collect { state ->
                    when (state) {
                        is CoursesViewModel.EditCourseState.Loading -> {
                            Log.d(TAG, "Loading course by id...")
                        }

                        is CoursesViewModel.EditCourseState.Success -> {
                            val course = state.courseData
                            binding.etCourseName.setText(course.course_name)
                            binding.etProgrammingLanguage.setText(course.programming_language)
                            binding.etCourseDescription.setText(course.course_description)
                            binding.tvCharCount.text = course.course_description.length.toString()
                        }

                        is CoursesViewModel.EditCourseState.Updated -> {
                            findNavController().popBackStack()
                        }

                        is CoursesViewModel.EditCourseState.Failure -> {
                            Log.e(
                                CoursesFragment.TAG,
                                "Error loading course by id: ${state.throwable}"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setupCharacterCount() {
        binding.etCourseDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val currentLength = it.length
                    binding.tvCharCount.text = "$currentLength/255"
                    if (currentLength > 255) {
                        val trimmedText = it.substring(0, 255)
                        binding.etCourseDescription.setText(trimmedText)
                        binding.etCourseDescription.setSelection(trimmedText.length)
                    }
                }
            }
        })
    }
}