package com.example.learnit.ui.feature.courses.courses.fragment

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
import com.example.learnit.data.ApiConstants.ARG_LESSON_ID
import com.example.learnit.data.courses.lessons.model.LessonData
import com.example.learnit.databinding.FragmentTheoryBinding
import com.example.learnit.ui.feature.courses.courses.TheoryAdapterListener
import com.example.learnit.ui.feature.courses.courses.adapter.TheoryAdapter
import com.example.learnit.ui.feature.courses.courses.viewModel.TheoryViewModel
import kotlinx.coroutines.launch

class TheoryFragment : Fragment(), TheoryAdapterListener {
    private val viewModel: TheoryViewModel by viewModels()
    private lateinit var binding: FragmentTheoryBinding
    private lateinit var currentLesson: LessonData

    companion object {
        val TAG: String = TheoryFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTheoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        observeState2()
        val lessonId = arguments?.getInt(ARG_LESSON_ID, -1) ?: -1
        viewModel.loadLessonContents(lessonId)
        viewModel.loadLessonById(lessonId)
        binding.imageViewBack.setOnClickListener {
            activity?.onBackPressed()
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
                                TheoryAdapter(state.lessonContentData, this@TheoryFragment)
                            binding.contentsRecyclerView.adapter = adapter
                        }

                        is TheoryViewModel.TheoryPageState.Failure -> {
                            Log.e(TAG, "Error loading lesson contents: ${state.throwable}")
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
        return currentLesson
    }

    override fun onBackToQuizClick() {
        parentFragmentManager.popBackStack()
    }
}