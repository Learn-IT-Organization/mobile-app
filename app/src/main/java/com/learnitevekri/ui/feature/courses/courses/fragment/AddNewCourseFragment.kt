package com.learnitevekri.ui.feature.courses.courses.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.learnitevekri.R
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.courses.course.model.AddNewCourseData
import com.learnitevekri.databinding.FragmentAddNewCourseBinding
import com.learnitevekri.ui.feature.courses.courses.viewModel.CoursesViewModel
import kotlinx.coroutines.launch

class AddNewCourseFragment : Fragment() {

    private val viewModel: CoursesViewModel by viewModels()
    private lateinit var binding: FragmentAddNewCourseBinding

    companion object {
        val TAG: String = AddNewCourseFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupCharacterCount()
        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupListeners() {
        binding.btnUploadCourse.setOnClickListener {
            val courseName = binding.etCourseName.text.toString()
            val programmingLanguage = binding.etProgrammingLanguage.text.toString()
            val courseDescription = binding.etCourseDescription.text.toString()
            val userId = SharedPreferences.getUserId()

            if (courseName.isEmpty() || programmingLanguage.isEmpty() || courseDescription.isEmpty()) {
                Snackbar.make(
                    requireView(), "Please fill in all fields", Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val addNewCourseData =
                AddNewCourseData(courseName, programmingLanguage, courseDescription, userId.toInt())

            lifecycleScope.launch {
                val courseId = viewModel.addNewCourse(addNewCourseData)
                Log.d(TAG, "Course ID: $courseId")
                if (courseId != null) {
                    binding.etCourseName.text.clear()
                    binding.etProgrammingLanguage.text.clear()
                    binding.etCourseDescription.text.clear()
                    val bundle = Bundle().apply {
                        putInt("course_id", courseId)
                    }
                    findNavController().navigate(
                        R.id.action_addNewCourseFragment_to_addNewChapterFragment, bundle
                    )
                } else {
                    Snackbar.make(
                        requireView(), "Failed to add new course", Snackbar.LENGTH_SHORT
                    ).show()
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