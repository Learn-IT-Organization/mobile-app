package com.learnitevekri.ui.feature.courses.courses.fragment

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
import com.learnitevekri.R
import com.learnitevekri.data.ApiConstants.ARG_LESSON_ID
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.courses.lessons.model.LessonData
import com.learnitevekri.databinding.FragmentTheoryBinding
import com.learnitevekri.ui.activities.MainActivity
import com.learnitevekri.ui.feature.courses.courses.TheoryAdapterListener
import com.learnitevekri.ui.feature.courses.courses.adapter.TheoryAdapter
import com.learnitevekri.ui.feature.courses.courses.viewModel.TheoryViewModel
import kotlinx.coroutines.launch

class TheoryFragment : Fragment(), TheoryAdapterListener {
    private val viewModel: TheoryViewModel by viewModels()
    private lateinit var binding: FragmentTheoryBinding
    private var currentLesson: LessonData? = null
    private var courseId = 1
    private var chapterId = 1

    companion object {
        val TAG: String = TheoryFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTheoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        observeState2()
        val lessonId = arguments?.getInt(ARG_LESSON_ID, -1) ?: -1
        courseId = arguments?.getInt("course_id", -1) ?: -1
        chapterId = arguments?.getInt("chapter_id", -1) ?: -1
        Log.d(TAG, "courseId: $courseId, chapterId: $chapterId, lessonId: $lessonId")
        viewModel.loadLessonContents(lessonId)
        viewModel.loadLessonById(lessonId)

        binding.imageViewBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.addContentButton.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("course_id", courseId)
                putInt("chapter_id", chapterId)
                putInt("lesson_id", lessonId)
            }
            findNavController().navigate(R.id.action_theoryFragment_to_simpleAddContentFragment, bundle)
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is TheoryViewModel.TheoryPageState.Loading -> {
                            Log.d(TAG, "Loading lesson contents...")
                        }

                        is TheoryViewModel.TheoryPageState.Success -> {
                            Log.d(TAG, "Lesson contents loaded")
                            val adapter =
                                TheoryAdapter(
                                    state.lessonContentData,
                                    this@TheoryFragment,
                                    SharedPreferences.getUserId().toInt()
                                )
                            binding.contentsRecyclerView.adapter = adapter
                        }

                        is TheoryViewModel.TheoryPageState.Failure -> {
                            Log.e(TAG, "Error loading lesson contents: ${state.throwable}")
                            (activity as MainActivity?)?.errorHandling(state.throwable)
                        }

                    }
                }
            }
        }
    }

    private fun observeState2() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state2.collect { state2 ->
                    when (state2) {
                        is TheoryViewModel.TheoryPageStateForLesson.Loading -> {
                            Log.d(TAG, "Loading lesson by id...")
                        }

                        is TheoryViewModel.TheoryPageStateForLesson.Success -> {
                            Log.d(TAG, "Lesson by id loaded")
                            currentLesson = state2.lessonData!!
                            Log.d(TAG, "lesson: $currentLesson")
                            Log.d(TAG, "lessonUserId: ${currentLesson?.lessonUserId}")
                            Log.d(
                                TAG,
                                "SharedPreferences.getUserId(): ${SharedPreferences.getUserId()}"
                            )
                            if (currentLesson?.lessonUserId == SharedPreferences.getUserId()
                                    .toInt()
                            ) {
                                binding.addContentButton.visibility = View.VISIBLE
                            } else {
                                binding.addContentButton.visibility = View.GONE
                            }
                        }

                        is TheoryViewModel.TheoryPageStateForLesson.Failure -> {
                            Log.e(TAG, "Error loading lesson by id: ${state2.throwable}")
                        }

                    }
                }
            }
        }
    }

    override fun getCurrentLesson(): LessonData {
        if (currentLesson == null) {
            throw IllegalStateException("Lesson is not loaded yet")
        }
        return currentLesson!!
    }

    override fun onBackToQuizClick() {
        parentFragmentManager.popBackStack()
    }

    override fun onDeleteContentClick(contentId: Int) {
        viewModel.deleteContent(contentId)
    }

    override fun onEditContentClick(contentId: Int) {
        val bundle = Bundle().apply {
            putInt("content_id", contentId)
        }
        findNavController().navigate(R.id.action_theoryFragment_to_simpleEditContentFragment, bundle)
    }
}