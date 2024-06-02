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
import com.learnitevekri.data.courses.chapters.model.AddNewChapterData
import com.learnitevekri.data.courses.chapters.model.EditChapterData
import com.learnitevekri.databinding.FragmentAddNewChapterBinding
import com.learnitevekri.ui.feature.courses.courses.ChapterItemClickListener
import com.learnitevekri.ui.feature.courses.courses.adapter.AddNewChapterAdapter
import com.learnitevekri.ui.feature.courses.courses.viewModel.ChaptersViewModel
import com.learnitevekri.ui.feature.courses.courses.viewModel.SharedLessonViewModel
import kotlinx.coroutines.launch

class AddNewChapterFragment : Fragment(), ChapterItemClickListener {

    private val viewModel: ChaptersViewModel by viewModels()
    private lateinit var binding: FragmentAddNewChapterBinding
    private lateinit var adapter: AddNewChapterAdapter
    private val chapters = mutableListOf<AddNewChapterData>()
    private var courseId = -1
    private var chapterId: Int? = null
    private val userId = SharedPreferences.getUserId()
    private val sharedViewModel: SharedLessonViewModel by viewModels({ requireParentFragment() })

    companion object {
        val TAG: String = AddNewChapterFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewChapterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        courseId = arguments?.getInt("course_id", -1) ?: -1
        Log.d(TAG, "Course ID: $courseId")
        if (chapters.isEmpty()) {
            setupEmptyChapter()
        }
        setupRecyclerView()
        binding.btnAddMoreChapter.setOnClickListener {
            onMoreChapterClick()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setupEmptyChapter() {
        chapters.add(AddNewChapterData(0, "", 0, "", 0, 0))
    }

    private fun setupRecyclerView() {
        adapter = AddNewChapterAdapter(chapters, this, sharedViewModel)
        binding.rvAddNewChapter.adapter = adapter
        binding.rvAddNewChapter.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun enableEditTextEditing() {
        val lastPosition = chapters.size - 1
        val viewHolder =
            binding.rvAddNewChapter.findViewHolderForAdapterPosition(lastPosition) as? AddNewChapterAdapter.ChapterViewHolder
        viewHolder?.enableEditing()
    }

    private fun disableEditTextEditing() {
        val lastPosition = chapters.size - 1
        val viewHolder =
            binding.rvAddNewChapter.findViewHolderForAdapterPosition(lastPosition) as? AddNewChapterAdapter.ChapterViewHolder
        viewHolder?.disableEditing()
    }

    override fun onEditClick(addNewChapterData: AddNewChapterData) {
        Log.d(TAG, "Edit clicked for chapter: $addNewChapterData")
        enableEditTextEditing()
    }

    override fun onSaveClick(currentChapterId: Int, editChapterData: EditChapterData) {
        lifecycleScope.launch {
            viewModel.editChapter(currentChapterId, editChapterData)
            Snackbar.make(requireView(), "Chapter updated successfully", Snackbar.LENGTH_SHORT)
                .show()
            disableEditTextEditing()
        }
    }

    override fun onAddLessonClick(currentPosition: Int) : Boolean{
        var success = false
        lifecycleScope.launch {
            val lastChapter = chapters[currentPosition]
            val chapterName = lastChapter.chapterName
            val chapterDescription = lastChapter.chapterDescription
            if (chapterName.isEmpty() || chapterDescription.isEmpty()) {
                Snackbar.make(
                    requireView(), "Please fill in all fields", Snackbar.LENGTH_SHORT
                ).show()
                return@launch
            }
            else {
                val newChapterData = AddNewChapterData(
                    0,
                    chapterName,
                    courseId,
                    chapterDescription,
                    currentPosition + 1,
                    userId.toInt()
                )
                chapters[chapters.size - 1] = newChapterData
                adapter.notifyItemChanged(chapters.size - 1)

                chapterId = viewModel.addNewChapter(newChapterData)

                if (chapterId == null) {
                    Snackbar.make(
                        requireView(), "Failed to add new chapter", Snackbar.LENGTH_SHORT
                    ).show()
                    return@launch
                } else {
                    success = true
                    chapterId?.let { it1 -> adapter.updateChapterId(chapters.size - 1, it1) }
                    disableEditTextEditing()
                    val bundle = Bundle().apply {
                        chapterId?.let { it1 -> putInt("chapter_id", it1) }
                    }
                    findNavController().navigate(
                        R.id.action_addNewChapterFragment_to_addNewLessonFragment, bundle
                    )
                }
            }
        }
        return success
    }

    private fun onMoreChapterClick() {
        chapters.add(AddNewChapterData(0, "", 0, "", 0, 0))
        adapter.notifyItemInserted(chapters.size - 1)
        binding.rvAddNewChapter.scrollToPosition(chapters.size - 1)
    }

}