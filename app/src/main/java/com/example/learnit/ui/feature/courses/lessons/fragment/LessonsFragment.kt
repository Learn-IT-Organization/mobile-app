package com.example.learnit.ui.feature.courses.lessons.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.learnit.databinding.FragmentLessonsBinding
import com.example.learnit.ui.feature.courses.lessons.adapter.LessonsAdapter
import com.example.learnit.ui.feature.courses.lessons.viewModel.LessonsViewModel
import kotlinx.coroutines.launch

class LessonsFragment : Fragment() {
    private val viewModel: LessonsViewModel by viewModels()
    private lateinit var binding: FragmentLessonsBinding

    companion object {
        val TAG: String = LessonsFragment::class.java.simpleName
        private const val ARG_CHAPTER_ID = "chapterId"
        fun newInstance(chapterId: Int?): LessonsFragment {
            val fragment = LessonsFragment()
            val args = Bundle()
            if (chapterId != null) {
                args.putInt(ARG_CHAPTER_ID, chapterId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLessonsBinding.inflate(inflater, container, false)
        val chapterId = arguments?.getInt(ARG_CHAPTER_ID, -1) ?: -1
        viewModel.loadLessons(chapterId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "Lessons fragment")
        observeState()
        binding.toolbar.setNavigationOnClickListener(
            View.OnClickListener {
                activity?.onBackPressed()
            }
        )
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is LessonsViewModel.LessonsPageState.Loading -> {
                            Log.d(TAG, "Loading lessons...")
                        }

                        is LessonsViewModel.LessonsPageState.Success -> {
                            Log.d(TAG, "Lessons loaded")
                            val adapter = LessonsAdapter(state.lessonData)
                            binding.lessonsRecycleView.adapter = adapter
                        }

                        is LessonsViewModel.LessonsPageState.Failure -> {
                            Log.e(TAG, "Error loading lessons: ${state.throwable}")
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}