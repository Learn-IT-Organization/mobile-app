package com.example.learnit.ui.feature.courses.chapters.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.learnit.R
import com.example.learnit.data.ApiConstants.ARG_CHAPTER_ID
import com.example.learnit.data.ApiConstants.ARG_COURSE_ID
import com.example.learnit.data.ApiConstants.ARG_LESSON_ID
import com.example.learnit.data.courses.chapters.model.ChapterData
import com.example.learnit.data.courses.lessons.model.LessonData
import com.example.learnit.databinding.DialogLessonCompletedBinding
import com.example.learnit.databinding.FragmentChaptersBinding
import com.example.learnit.ui.feature.courses.chapters.adapter.ChaptersAdapter
import com.example.learnit.ui.feature.courses.chapters.viewModel.ChaptersViewModel
import com.example.learnit.ui.feature.courses.chapters.viewModel.LessonsViewModel
import kotlinx.coroutines.launch

class ChaptersFragment : Fragment(), ChaptersAdapter.OnItemClickListener {

    private val viewModel: ChaptersViewModel by activityViewModels()
    private val lessonViewModel: LessonsViewModel by activityViewModels()

    private lateinit var binding: FragmentChaptersBinding

    companion object {
        val TAG: String = ChaptersFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChaptersBinding.inflate(inflater, container, false)
        binding.imageViewBack.setOnClickListener {
            activity?.onBackPressed()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        val courseId = arguments?.getInt(ARG_COURSE_ID, -1) ?: -1
        val lessonId = arguments?.getInt(ARG_LESSON_ID, -1) ?: -1
        viewModel.loadChapters(courseId)
        lessonViewModel.loadLessonResult(lessonId)
        observeLessonResult()
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
                            binding.chaptersRecyclerView.adapter = adapter
                        }

                        is ChaptersViewModel.ChaptersScreenState.Failure -> {
                            Log.e(TAG, "Error loading chapters: ${state.throwable}")
                        }
                    }
                }
            }
        }
    }

    private fun observeLessonResult() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                lessonViewModel.state.collect { state ->
                    when (state) {
                        is LessonsViewModel.LessonScreenState.Loading -> {
                            Log.d(TAG, "Loading lesson result...")
                        }

                        is LessonsViewModel.LessonScreenState.Success -> {
                            Log.d(TAG, "Lesson result loaded")
                        }

                        is LessonsViewModel.LessonScreenState.Failure -> {
                            Log.e(TAG, "Error loading lesson result: ${state.throwable}")
                        }
                    }
                }
            }
        }
    }

    override fun onChapterItemClick(chapter: ChapterData) {
        TODO()
    }

    override fun onPlayStateClick(lesson: LessonData) {
        if (lesson.isCompleted) {
            showDialogLessonCompleted(lesson)
        } else {

            val bundle = Bundle().apply {
                putInt(ARG_COURSE_ID, arguments?.getInt(ARG_COURSE_ID, -1) ?: -1)
                putInt(ARG_CHAPTER_ID, lesson.lessonChapterId)
                putInt(ARG_LESSON_ID, lesson.lessonId)
            }

            findNavController().navigate(
                R.id.action_chaptersFragment_to_quizFragment,
                bundle
            )
        }
    }

    override fun onTheoryClick(lesson: LessonData) {

        val bundle = Bundle().apply {
            putInt(ARG_LESSON_ID, lesson.lessonId)
        }

        findNavController().navigate(
            R.id.action_chaptersFragment_to_theoryFragment,
            bundle
        )
    }

    private fun showDialogLessonCompleted(lesson: LessonData) {
        val inflater = LayoutInflater.from(requireContext())
        val view = DialogLessonCompletedBinding.inflate(inflater).root

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)

        view.findViewById<TextView>(R.id.scoreTextView).text =
            getString(R.string.your_score_is, lesson.lessonScore.toInt().toString())
        val dialog = builder.create()
        
        dialog.show()

        view.findViewById<Button>(R.id.yesButton).setOnClickListener {
            val bundle = Bundle().apply {
                putInt(ARG_COURSE_ID, arguments?.getInt(ARG_COURSE_ID, -1) ?: -1)
                putInt(ARG_CHAPTER_ID, lesson.lessonChapterId)
                putInt(ARG_LESSON_ID, lesson.lessonId + 1)
            }

            findNavController().navigate(
                R.id.action_chaptersFragment_to_quizFragment,
                bundle
            )
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.noButton).setOnClickListener {
            dialog.dismiss()
        }
    }
}