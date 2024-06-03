package com.learnitevekri.ui.feature.courses.courses.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.learnitevekri.data.courses.lessons.model.EditLessonData
import com.learnitevekri.databinding.FragmentEditLessonBinding
import com.learnitevekri.ui.feature.courses.courses.viewModel.LessonsViewModel
import kotlinx.coroutines.launch

class EditLessonFragment : Fragment() {
    private val viewModel: LessonsViewModel by viewModels()
    private lateinit var binding: FragmentEditLessonBinding

    companion object {
        val TAG: String = EditLessonFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lessonId = arguments?.getInt("lessonId")
        if (lessonId != null) {
            viewModel.loadLessonById(lessonId)
        }

        observeState()
        setupSpinner()
        setupCharacterCount()

        binding.btnSave.setOnClickListener {
            val updatedLesson = EditLessonData(
                lessonName = binding.etLessonName.text.toString(),
                lessonDescription = binding.etLessonDescription.text.toString(),
                lessonType = binding.spinnerLessonType.selectedItem.toString(),
                lessonTags = binding.etLessonTags.text.toString(),
            )
            if (lessonId != null) {
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        viewModel.editLesson(lessonId, updatedLesson)
                        Snackbar.make(
                            requireView(), "Lesson updated successfully", Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    } catch (e: Exception) {
                        Snackbar.make(
                            requireView(), "Failed to update lesson", Snackbar.LENGTH_SHORT
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
                        is LessonsViewModel.EditLessonState.Loading -> {
                            Log.d(EditCourseFragment.TAG, "Loading lesson by id...")
                        }

                        is LessonsViewModel.EditLessonState.Success -> {
                            val course = state.lessonData
                            binding.etLessonName.setText(course.lessonName)
                            binding.etLessonDescription.setText(course.lessonDescription)
                            binding.etLessonTags.setText(course.lessonTags)
                            binding.tvCharCount.text = course.lessonDescription.length.toString()
                            setSpinnerSelection(course.lessonType)
                        }

                        is LessonsViewModel.EditLessonState.Updated -> {
                            findNavController().popBackStack()
                        }

                        is LessonsViewModel.EditLessonState.Failure -> {
                            Log.e(
                                TAG,
                                "Error loading lesson by id: ${state.throwable}"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setupCharacterCount() {
        binding.etLessonDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val currentLength = it.length
                    binding.tvCharCount.text = "$currentLength/255"
                    if (currentLength > 255) {
                        val trimmedText = it.substring(0, 255)
                        binding.etLessonDescription.setText(trimmedText)
                        binding.etLessonDescription.setSelection(trimmedText.length)
                    }
                }
            }
        })
    }

    private fun setupSpinner() {
        val lessonTypes = listOf("theory", "exercise")
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, lessonTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerLessonType.adapter = adapter
    }

    private fun setSpinnerSelection(value: String) {
        val adapter = binding.spinnerLessonType.adapter as ArrayAdapter<String>
        val position = adapter.getPosition(value)
        binding.spinnerLessonType.setSelection(position)
    }
}