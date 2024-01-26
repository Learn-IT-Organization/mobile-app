package com.example.learnit.ui.feature.courses.lessons.fragment

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
import com.example.learnit.databinding.FragmentLessonsBinding
import com.example.learnit.ui.feature.courses.chapters.fragment.ChaptersFragment.Companion.ARG_COURSE_ID
import com.example.learnit.ui.feature.courses.lessons.adapter.LessonsAdapter
import com.example.learnit.ui.feature.courses.lessons.model.LessonModel
import com.example.learnit.ui.feature.courses.lessons.viewModel.LessonsViewModel
import kotlinx.coroutines.launch

class LessonsFragment : Fragment() {
    private val viewModel: LessonsViewModel by viewModels()
    private lateinit var binding: FragmentLessonsBinding

    companion object {
        val TAG: String = LessonsFragment::class.java.simpleName
        private const val ARG_CHAPTER_ID = "chapterId"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLessonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        val chapterId = arguments?.getInt(ARG_CHAPTER_ID, -1) ?: -1
        val courseId = arguments?.getInt(ARG_COURSE_ID, -1) ?: -1
        viewModel.loadLessons(chapterId, courseId)

        binding.toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

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
                            val adapter = LessonsAdapter(
                                state.lessonData,
                                object : LessonsAdapter.OnLessonItemClickListener {
                                    override fun onLessonItemClick(lesson: LessonModel) {
                                        this@LessonsFragment.onLessonItemClick(lesson)
                                    }
                                })
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


    fun onLessonItemClick(lesson: LessonModel) {
        val bundle = Bundle().apply {
            putInt("courseId", arguments?.getInt(ARG_COURSE_ID, -1) ?: -1)
            putInt("chapterId", lesson.lessonChapterId)
            putInt("lessonId", lesson.lessonId)
        }

        findNavController().navigate(
            R.id.action_lessonsFragment_to_quizFragment,
            bundle
        )
    }

}