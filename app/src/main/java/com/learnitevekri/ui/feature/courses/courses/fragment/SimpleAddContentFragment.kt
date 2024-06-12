package com.learnitevekri.ui.feature.courses.courses.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.courses.lessons.model.LessonContentData
import com.learnitevekri.databinding.FragmentSimpleAddContentBinding
import com.learnitevekri.ui.feature.courses.courses.viewModel.AddContentViewModel
import kotlinx.coroutines.launch

class SimpleAddContentFragment : Fragment() {

    private val viewModel: AddContentViewModel by viewModels()
    private lateinit var binding: FragmentSimpleAddContentBinding
    private val contents = mutableListOf<LessonContentData>()
    private var lessonId = -1
    private var chapterId = -1
    private var courseId = -1
    private var userId = -1

    companion object {
        val TAG: String = SimpleAddContentFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSimpleAddContentBinding.inflate(inflater, container, false)
        userId = SharedPreferences.getUserId().toInt()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        courseId = arguments?.getInt("course_id", -1) ?: -1
        chapterId = arguments?.getInt("chapter_id", -1) ?: -1
        lessonId = arguments?.getInt("lesson_id", -1) ?: -1

        Log.d(TAG, "Course ID, Chapter ID, Lesson ID: $courseId, $chapterId, $lessonId")

        setupEmptyContent()
        setupSaveButton()
    }

    private fun setupEmptyContent() {
        contents.add(LessonContentData(0, "", "", 0, "", "", 0))
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            if (checkAllFieldsFilled()) {
                lifecycleScope.launch {
                    performAddOperation()
                    findNavController().navigateUp()
                }
            } else {
                Snackbar.make(requireView(), "Please fill in all fields", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkAllFieldsFilled(): Boolean {
        return binding.etContentTitle.text.toString().isNotEmpty() &&
                binding.etContentDescription.text.toString().isNotEmpty() &&
                binding.etContentUrl.text.toString().isNotEmpty()
    }

    private suspend fun performAddOperation() {
        val contentType = binding.spinnerContentType.selectedItem.toString()
        val newContentData = LessonContentData(
            0,
            contentType,
            binding.etContentUrl.text.toString(),
            lessonId,
            binding.etContentDescription.text.toString(),
            binding.etContentTitle.text.toString(),
            userId
        )

        val newContentId = viewModel.createLessonContent(newContentData)

        if (newContentId != null) {
            Snackbar.make(requireView(), "Lesson content added successfully", Snackbar.LENGTH_SHORT)
                .show()
            findNavController().navigateUp()
        } else {
            Snackbar.make(requireView(), "Failed to add new lesson content", Snackbar.LENGTH_SHORT)
                .show()
        }
    }
}