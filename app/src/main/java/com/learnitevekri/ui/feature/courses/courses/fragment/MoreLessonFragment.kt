package com.learnitevekri.ui.feature.courses.courses.fragment

import android.R
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.courses.lessons.model.AddNewLessonData
import com.learnitevekri.databinding.FragmentMoreLessonBinding
import com.learnitevekri.ui.feature.courses.courses.viewModel.LessonsViewModel
import kotlinx.coroutines.launch

class MoreLessonFragment : Fragment() {
    companion object {
        val TAG: String = MoreLessonFragment::class.java.simpleName
    }

    private val viewModel: LessonsViewModel by viewModels()
    private lateinit var binding: FragmentMoreLessonBinding
    private val userId = SharedPreferences.getUserId()
    private var courseId = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMoreLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chapterId = arguments?.getInt("chapter_id")
        val lessonSize = arguments?.getInt("lessonSize")
        courseId = arguments?.getInt("course_id") ?: 1
        Log.d(TAG, "Chapter ID: $chapterId")
        Log.d(TAG, "Lesson Size: $lessonSize")
        Log.d(TAG, "Course ID: $courseId")
        val nextLessonSequenceNumber = lessonSize?.plus(1)
        setupSpinner()
        setupCharacterCount()

        binding.btnSave.setOnClickListener {
            val newLessonData = AddNewLessonData(
                0,
                binding.etLessonName.text.toString(),
                chapterId!!,
                nextLessonSequenceNumber!!,
                binding.etLessonDescription.text.toString(),
                binding.spinnerLessonType.selectedItem.toString(),
                binding.etLessonTags.text.toString(),
                userId.toInt()
            )
            lifecycleScope.launch {
                val lessonId = viewModel.addNewLesson(newLessonData)
                if (lessonId != null) {
                    Snackbar.make(requireView(), "Lesson added successfully", Snackbar.LENGTH_SHORT)
                        .show()
                    if (binding.spinnerLessonType.selectedItem.toString() == "exercise") {
                        val bundle = Bundle().apply {
                            putInt("course_id", courseId)
                            putInt("chapter_id", chapterId)
                            putInt("lesson_id", lessonId)
                        }
                        findNavController().navigate(
                            com.learnitevekri.R.id.action_moreLessonFragment_to_addQuestionFragment,
                            bundle
                        )
                    } else {
                        findNavController().popBackStack()
                    }
                } else {
                    Snackbar.make(requireView(), "Failed to add lesson", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupSpinner() {
        val lessonTypes = listOf("theory", "exercise")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, lessonTypes)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerLessonType.adapter = adapter
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

}