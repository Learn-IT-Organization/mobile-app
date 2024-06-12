package com.learnitevekri.ui.feature.courses.courses.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.learnitevekri.R
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.courses.lessons.model.EditLessonContentData
import com.learnitevekri.data.courses.lessons.model.LessonContentData
import com.learnitevekri.databinding.FragmentAddContentBinding
import com.learnitevekri.ui.feature.courses.courses.LessonContentClickListener
import com.learnitevekri.ui.feature.courses.courses.adapter.AddLessonContentAdapter
import com.learnitevekri.ui.feature.courses.courses.viewModel.AddContentViewModel
import kotlinx.coroutines.launch

class AddContentFragment : Fragment(), LessonContentClickListener {

    private val viewModel: AddContentViewModel by viewModels()
    private lateinit var binding: FragmentAddContentBinding
    private lateinit var adapter: AddLessonContentAdapter
    private val contents = mutableListOf<LessonContentData>()
    private var lessonId = -1
    private var chapterId = -1
    private var courseId = -1
    private var lessonType: String? = null
    private var userId = -1

    companion object {
        val TAG: String = AddContentFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddContentBinding.inflate(inflater, container, false)
        userId = SharedPreferences.getUserId().toInt()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        courseId = arguments?.getInt("course_id", -1) ?: -1
        chapterId = arguments?.getInt("chapter_id", -1) ?: -1
        lessonId = arguments?.getInt("lesson_id", -1) ?: -1
        lessonType = arguments?.getString("lesson_type")

        Log.d(TAG, "Course ID, Chapter ID, Lesson ID: $courseId, $chapterId, $lessonId")
        if (lessonType == "theory") {
            binding.btnSaveAndAddQuestion.visibility = View.GONE
        }

        if (contents.isEmpty()) {
            setupEmptyContent()
        }

        setupRecyclerView()
        setupAddContentButton()
        checkAllFieldsFilled()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.btnSaveAndBackToLessons.setOnClickListener {
            if (checkAllFieldsFilled()) {
                lifecycleScope.launch {
                    performAddOperation()
                }
                findNavController().navigateUp()
            } else {
                Snackbar.make(requireView(), "Please fill in all fields", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setupEmptyContent() {
        contents.add(LessonContentData(0, "", "", 0, "", "", 0))
    }

    private fun setupRecyclerView() {
        adapter = AddLessonContentAdapter(contents, this)
        binding.rvAddContent.adapter = adapter
        binding.rvAddContent.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupAddContentButton() {
        binding.btnSaveAndAddQuestion.setOnClickListener {
            if (checkAllFieldsFilled()) {
                lifecycleScope.launch {
                    performAddOperation {
                        navigateToAddQuestionFragment(lessonId)
                    }
                    disableEditTextEditing(contents.size - 1)
                }
            } else {
                Snackbar.make(requireView(), "Please fill in all fields", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        binding.btnSaveAndAddAnother.setOnClickListener {
            if (checkAllFieldsFilled()) {
                lifecycleScope.launch {
                    performAddOperation()
                    contents.add(LessonContentData(0, "", "", 0, "", "", 0))
                    adapter.notifyItemInserted(contents.size - 1)
                    binding.rvAddContent.scrollToPosition(contents.size - 1)
                    checkAllFieldsFilled()
                }
            } else {
                Snackbar.make(requireView(), "Please fill in all fields", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun disableEditTextEditing(position: Int) {
        val viewHolder =
            binding.rvAddContent.findViewHolderForAdapterPosition(position) as? AddLessonContentAdapter.AddLessonContentViewHolder
        viewHolder?.disableEditing()
    }

    private fun enableEditTextEditing(position: Int) {
        val viewHolder =
            binding.rvAddContent.findViewHolderForAdapterPosition(position) as? AddLessonContentAdapter.AddLessonContentViewHolder
        viewHolder?.enableEditing()
    }

    private suspend fun performAddOperation(onSuccess: ((Int) -> Unit)? = null) {
        val lastContent = contents.last()
        val contentTitle = lastContent.contentTitle
        val contentDescription = lastContent.contentDescription
        val contentType = lastContent.contentType
        val contentUrl = lastContent.url

        if (contentUrl.isEmpty() || contentType.isEmpty() || contentDescription.isEmpty() || contentTitle.isEmpty()) {
            Snackbar.make(
                requireView(), "Please fill in all fields", Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        val newContentData = LessonContentData(
            0,
            contentType,
            contentUrl,
            220,
            contentTitle,
            contentDescription,
            userId
        )
        val newContentIndex = contents.size - 1
        contents[newContentIndex] = newContentData
        adapter.notifyItemChanged(newContentIndex)

        val newContentId = viewModel.createLessonContent(newContentData)

        if (newContentId != null) {
            Snackbar.make(requireView(), "Lesson content added successfully", Snackbar.LENGTH_SHORT)
                .show()
            contents[newContentIndex].contentId = newContentId

            Log.d(TAG, "New content id: $newContentId")

            adapter.updateContentId(newContentIndex, newContentId)
            disableEditTextEditing(newContentIndex)
            onSuccess?.invoke(newContentId)
        } else {
            Snackbar.make(requireView(), "Failed to add new lesson content", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun navigateToAddQuestionFragment(lessonId: Int) {
        val bundle = Bundle().apply {
            putInt("course_id", courseId)
            putInt("chapter_id", chapterId)
            putInt("lesson_id", lessonId)
        }
        findNavController().navigate(
            R.id.action_addContentFragment_to_addQuestionFragment,
            bundle
        )
    }

    private fun checkAllFieldsFilled(): Boolean {
        for (content in contents) {
            if (content.contentTitle.isEmpty() || content.contentDescription.isEmpty()
                || content.url.isEmpty() || content.contentType.isEmpty()
            ) {
                return false
            }
        }
        return true
    }

    override fun onAddContentClick(editLessonContentData: LessonContentData) {
        val position = contents.indexOf(editLessonContentData)
        if (position != -1) {
            enableEditTextEditing(position)
        }
    }

    override fun onSaveClick(
        currentLessonContentId: Int,
        editLessonContentData: EditLessonContentData,
    ) {
        lifecycleScope.launch {
            Log.d(TAG, "Saving content with id: $currentLessonContentId")
            viewModel.editLessonContent(currentLessonContentId, editLessonContentData)
            Snackbar.make(
                requireView(),
                "Lesson content updated successfully",
                Snackbar.LENGTH_SHORT
            )
                .show()
            Log.d(TAG, "Lesson content updated successfully")
            val position = contents.indexOfFirst { it.contentId == currentLessonContentId }
            if (position != -1) {
                disableEditTextEditing(position)
            }
        }
    }
}