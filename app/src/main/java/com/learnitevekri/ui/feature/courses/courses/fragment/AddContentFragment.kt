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
import com.google.android.material.snackbar.Snackbar
import com.learnitevekri.R
import com.learnitevekri.data.courses.lessons.model.EditLessonContentData
import com.learnitevekri.data.courses.lessons.model.LessonContentData
import com.learnitevekri.databinding.FragmentAddContentBinding
import com.learnitevekri.ui.feature.courses.courses.LessonContentClickListener
import com.learnitevekri.ui.feature.courses.courses.adapter.AddLessonContentAdapter
import com.learnitevekri.ui.feature.courses.courses.viewModel.AddContentViewModel
import kotlinx.coroutines.launch

class AddContentFragment : Fragment(), LessonContentClickListener {

    private val viewModel: AddContentViewModel by viewModels({ requireParentFragment() })
    private lateinit var binding: FragmentAddContentBinding
    private lateinit var adapter: AddLessonContentAdapter
    private val contents = mutableListOf<LessonContentData>()
    private var courseId = -1
    private var contentId: Int? = null
    private var lessonId: Int? = null

    companion object {
        val TAG: String = AddContentFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArguments()
        setupViews()
        setupListeners()
        setupOnBackPressedCallback()
    }

    private fun setupArguments() {
        courseId = arguments?.getInt("course_id", -1) ?: -1
        lessonId = arguments?.getInt("lesson_id", -1) ?: -1
        Log.d(TAG, "Course ID, lessonId: $courseId, $lessonId")
        if (contents.isEmpty()) {
            contents.add(LessonContentData(0, "", "", 0, "", ""))
        }
    }

    private fun setupViews() {
        adapter = AddLessonContentAdapter(contents, this, viewModel)
        binding.rvAddContent.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnAddContent.setOnClickListener { onMoreContentClick() }
        binding.btnSaveAndAddAnother.setOnClickListener { onSaveAndAddAnotherClick() }
    }

    private fun setupOnBackPressedCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d(TAG, "Back pressed")
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun onMoreContentClick() {
        contents.add(LessonContentData(0, "", "", 0, "", ""))
        adapter.notifyItemInserted(contents.size - 1)
        disableEditTextEditing()
        binding.rvAddContent.scrollToPosition(contents.size - 1)
    }

    override fun onEditClick(editLessonContentData: LessonContentData) {
        enableEditTextEditing()
    }

    override fun onSaveClick(
        currentLessonContentId: Int,
        editLessonContentData: EditLessonContentData
    ) {
        lifecycleScope.launch {
            viewModel.editLessonContent(currentLessonContentId, editLessonContentData)
            Snackbar.make(
                requireView(),
                "Lesson content updated successfully",
                Snackbar.LENGTH_SHORT
            ).show()
            disableEditTextEditing()
        }
    }

    override fun onAddQuestionClick(currentPosition: Int): Boolean {
        var success = false
        lifecycleScope.launch {
            val lastContent = contents[currentPosition]
            val contentType = lastContent.contentType
            val url = lastContent.url
            val contentTitle = lastContent.contentTitle
            val contentDescription = lastContent.contentDescription

            if (url.isEmpty() || contentDescription.isEmpty() || contentTitle.isEmpty() || contentType.isEmpty()) {
                Snackbar.make(
                    requireView(), "Please fill in all fields", Snackbar.LENGTH_SHORT
                ).show()
                return@launch
            } else {
                val newContentData = lessonId?.let {
                    LessonContentData(
                        0,
                        contentType,
                        url,
                        it,
                        contentTitle,
                        contentDescription
                    )
                }

                contents[contents.size - 1] = newContentData!!
                adapter.notifyItemChanged(contents.size - 1)

                contentId = viewModel.createLessonContent(newContentData)

                if (contentId == null) {
                    Snackbar.make(
                        requireView(), "Failed to add new content", Snackbar.LENGTH_SHORT
                    ).show()
                    return@launch
                } else {
                    success = true
                    contentId?.let { it1 -> adapter.updateContentId(contents.size - 1, it1) }
                    disableEditTextEditing()
                    val bundle = Bundle().apply {
                        contentId?.let { it1 -> putInt("content_id", it1) }
                    }
                    findNavController().navigate(
                        R.id.action_addContentFragment_to_addQuestionFragment, bundle
                    )
                }
            }
        }
        return success
    }

    private fun onSaveAndAddAnotherClick() {
        val lastPosition = contents.size - 1
        val lastContent = contents[lastPosition]
        val contentType = lastContent.contentType
        val url = lastContent.url
        val contentTitle = lastContent.contentTitle
        val contentDescription = lastContent.contentDescription

        if (url.isEmpty() || contentDescription.isEmpty() || contentTitle.isEmpty() || contentType.isEmpty()) {
            Snackbar.make(
                requireView(), "Please fill in all fields", Snackbar.LENGTH_SHORT
            ).show()
            return
        } else {
            val newContentData = lessonId?.let {
                LessonContentData(
                    0,
                    contentType,
                    url,
                    it,
                    contentTitle,
                    contentDescription
                )

            }
            contents[lastPosition] = newContentData!!
            adapter.notifyItemChanged(lastPosition)
            disableEditTextEditing()

            contents.add(LessonContentData(0, "", "", 0, "", ""))
            adapter.notifyItemInserted(lastPosition + 1)
            binding.rvAddContent.scrollToPosition(lastPosition + 1)

        }
    }

    private fun enableEditTextEditing() {
        val lastPosition = contents.size - 1
        val viewHolder =
            binding.rvAddContent.findViewHolderForAdapterPosition(lastPosition) as? AddLessonContentAdapter.AddLessonContentViewHolder
        viewHolder?.showEditMode()
    }

    private fun disableEditTextEditing() {
        val lastPosition = contents.size - 1
        val viewHolder =
            binding.rvAddContent.findViewHolderForAdapterPosition(lastPosition) as? AddLessonContentAdapter.AddLessonContentViewHolder
        viewHolder?.showViewMode()
    }
}