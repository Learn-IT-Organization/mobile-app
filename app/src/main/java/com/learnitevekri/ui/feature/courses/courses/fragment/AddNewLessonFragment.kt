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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.learnitevekri.R
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.courses.lessons.model.AddNewLessonData
import com.learnitevekri.data.courses.lessons.model.EditLessonData
import com.learnitevekri.databinding.FragmentAddNewLessonBinding
import com.learnitevekri.ui.feature.courses.courses.LessonItemClickListener
import com.learnitevekri.ui.feature.courses.courses.adapter.AddNewLessonAdapter
import com.learnitevekri.ui.feature.courses.courses.viewModel.LessonsViewModel
import kotlinx.coroutines.launch

class AddNewLessonFragment : Fragment(), LessonItemClickListener {

    private val viewModel: LessonsViewModel by viewModels()
    private lateinit var binding: FragmentAddNewLessonBinding
    private lateinit var adapter: AddNewLessonAdapter
    private val lessons = mutableListOf<AddNewLessonData>()
    private var chapterId = -1
    private val userId = SharedPreferences.getUserId()

    companion object {
        val TAG: String = AddNewLessonFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chapterId = arguments?.getInt("chapter_id", -1) ?: -1
        Log.d(TAG, "Chapter ID: $chapterId")
        if (lessons.isEmpty()) {
            setupEmptyLesson()
        }
        setupRecyclerView()
        setupAddLessonButton()
        checkAllFieldsFilled()
    }

    private fun setupEmptyLesson() {
        lessons.add(AddNewLessonData(0, "", 0, 0, "", "", "", 0))
    }

    private fun setupRecyclerView() {
        adapter = AddNewLessonAdapter(lessons, this)
        binding.rvAddNewLesson.adapter = adapter
        binding.rvAddNewLesson.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupAddLessonButton() {
        binding.btnAddContent.setOnClickListener {
            if (checkAllFieldsFilled()) {
                lifecycleScope.launch {
                    performAddOperation { lessonId ->
                        navigateToAddContentFragment(chapterId, lessonId)
                    }
                    disableEditTextEditing(lessons.size - 1)
                }
            } else {
                Snackbar.make(requireView(), "Please fill in all fields", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.btnAddMoreLesson.setOnClickListener {
            if (checkAllFieldsFilled()) {
                lifecycleScope.launch {
                    performAddOperation()
                    lessons.add(AddNewLessonData(0, "", 0, 0, "", "", "", 0))
                    adapter.notifyItemInserted(lessons.size - 1)
                    binding.rvAddNewLesson.scrollToPosition(lessons.size - 1)
                    checkAllFieldsFilled()
                }
            } else {
                Snackbar.make(requireView(), "Please fill in all fields", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.btnBackToChapter.setOnClickListener {
            if (checkAllFieldsFilled()) {
                lifecycleScope.launch {
                    performAddOperation()
                }
                findNavController().navigateUp()
            } else {
                Snackbar.make(requireView(), "Please fill in all fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun disableEditTextEditing(position: Int) {
        val viewHolder = binding.rvAddNewLesson.findViewHolderForAdapterPosition(position) as? AddNewLessonAdapter.LessonViewHolder
        viewHolder?.disableEditing()
    }

    private fun enableEditTextEditing(position: Int) {
        val viewHolder = binding.rvAddNewLesson.findViewHolderForAdapterPosition(position) as? AddNewLessonAdapter.LessonViewHolder
        viewHolder?.enableEditing()
    }

    override fun onEditClick(addNewLessonData: AddNewLessonData) {
        Log.d(TAG, "Edit clicked for lesson: $addNewLessonData")
        val position = lessons.indexOf(addNewLessonData)
        if (position != -1) {
            enableEditTextEditing(position)
        }
    }

    override fun onSaveClick(currentLessonId: Int, editLessonData: EditLessonData) {
        lifecycleScope.launch {
            viewModel.editLesson(currentLessonId, editLessonData)
            Snackbar.make(requireView(), "Lesson updated successfully", Snackbar.LENGTH_SHORT).show()
            val position = lessons.indexOfFirst { it.lessonId == currentLessonId }
            if (position != -1) {
                disableEditTextEditing(position)
            }
        }
    }

    private suspend fun performAddOperation(onSuccess: ((Int) -> Unit)? = null) {
        val lastLesson = lessons.last()
        val lessonName = lastLesson.lessonName
        val lessonDescription = lastLesson.lessonDescription
        val lessonType = lastLesson.lessonType
        val lessonTags = lastLesson.lessonTags
        if (lessonName.isEmpty() || lessonDescription.isEmpty() || lessonType.isEmpty() || lessonTags.isEmpty()) {
            Snackbar.make(
                requireView(), "Please fill in all fields", Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        val newLessonData = AddNewLessonData(
            0,
            lessonName,
            chapterId,
            lessons.size,
            lessonDescription,
            lessonType,
            lessonTags,
            userId.toInt()
        )
        val newLessonIndex = lessons.size - 1
        lessons[newLessonIndex] = newLessonData
        adapter.notifyItemChanged(newLessonIndex)

        val newLessonId = viewModel.addNewLesson(newLessonData)
        Log.d(TAG, "Lesson ID: $newLessonId")

        if (newLessonId != null) {
            Snackbar.make(requireView(), "Lesson added successfully", Snackbar.LENGTH_SHORT)
                .show()
            lessons[newLessonIndex].lessonId = newLessonId
            adapter.updateLessonId(newLessonIndex, newLessonId)
            disableEditTextEditing(newLessonIndex)
            onSuccess?.invoke(newLessonId)
        } else {
            Snackbar.make(requireView(), "Failed to add new lesson", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun navigateToAddContentFragment(chapterId: Int, lessonId: Int) {
        val bundle = Bundle().apply {
            putInt("chapter_id", chapterId)
            putInt("lesson_id", lessonId)
        }
        findNavController().navigate(R.id.action_addNewLessonFragment_to_addContentFragment, bundle)
    }

    private fun checkAllFieldsFilled(): Boolean {
        for (lesson in lessons) {
            if (lesson.lessonName.isEmpty() ||
                lesson.lessonDescription.isEmpty() ||
                lesson.lessonType.isEmpty() ||
                lesson.lessonTags.isEmpty()) {
                return false
            }
        }
        return true
    }
}