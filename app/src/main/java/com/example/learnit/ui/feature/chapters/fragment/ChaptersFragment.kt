package com.example.learnit.ui.feature.chapters.fragment

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
import com.example.learnit.R
import com.example.learnit.data.ApiConstants
import com.example.learnit.databinding.FragmentChaptersBinding
import com.example.learnit.ui.feature.chapters.adapter.ChaptersAdapter
import com.example.learnit.ui.feature.chapters.model.ChapterModel
import com.example.learnit.ui.feature.chapters.viewModel.ChaptersViewModel
import com.example.learnit.ui.feature.courses.lessons.fragment.LessonsFragment
import kotlinx.coroutines.launch

class ChaptersFragment : Fragment() {

    private lateinit var binding: FragmentChaptersBinding
class ChaptersFragment : Fragment(), ChaptersAdapter.OnChapterItemClickListener {
    private val viewModel: ChaptersViewModel by viewModels()
    private var _binding: FragmentChaptersBinding? = null
    private val binding get() = _binding!!

    companion object {
        val TAG: String = ChaptersFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChaptersBinding.inflate(inflater, container, false)
        binding.toolbar.setNavigationOnClickListener(
            View.OnClickListener {
                activity?.onBackPressed()
            }
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        val courseId: Int? = arguments?.getInt(ApiConstants.COURSE_ID)
        Log.d("ChaptersFragment", "courseId: $courseId")
        if (courseId != null) {
            viewModel.loadChapters(courseId.toInt())
        }
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
                            val adapter = ChaptersAdapter(state.chaptersData)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onChapterItemClick(chapter: ChapterModel) {
        val action = ChaptersFragmentDirections.actionChaptersFragmentToLessonsFragment(chapter.chapterId!!)
        findNavController().navigate(action)
    }


}