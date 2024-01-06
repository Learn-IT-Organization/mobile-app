package com.example.learnit.ui.feature.courses.chapters.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.learnit.databinding.FragmentChaptersBinding
import com.example.learnit.ui.feature.courses.chapters.adapter.ChaptersAdapter
import com.example.learnit.ui.feature.courses.chapters.model.ChapterModel
import com.example.learnit.ui.feature.courses.chapters.viewModel.ChaptersViewModel
import kotlinx.coroutines.launch

class ChaptersFragment : Fragment(), ChaptersAdapter.OnChapterItemClickListener {

    private val viewModel: ChaptersViewModel by viewModels()
    private lateinit var binding: FragmentChaptersBinding

    companion object {
        val TAG: String = ChaptersFragment::class.java.simpleName
        const val ARG_COURSE_ID = "courseId"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentChaptersBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        val courseId = arguments?.getInt(ARG_COURSE_ID, -1) ?: -1
        viewModel.loadChapters(courseId)
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is ChaptersViewModel.ChaptersScreenState.Loading -> {
                            Log.d(TAG, "Loading chapters...")
                        }

                        is ChaptersViewModel.ChaptersScreenState.Success -> {
                            Log.d(TAG, "Chapters loaded")
                            val adapter = ChaptersAdapter(state.chaptersData, this@ChaptersFragment)
                            binding.coursesRecycleView.adapter = adapter
                        }

                        is ChaptersViewModel.ChaptersScreenState.Failure -> {
                            Log.e(TAG, "Error loading chapters: ${state.throwable}")
                        }
                    }
                }
            }
        }
    }

    override fun onChapterItemClick(chapter: ChapterModel) {
        val action =
            ChaptersFragmentDirections.actionChaptersFragmentToLessonsFragment(
                chapter.chapterId!!,
                chapter.chapterCourseId!!,
                0
            )
        findNavController().navigate(action)
    }

}