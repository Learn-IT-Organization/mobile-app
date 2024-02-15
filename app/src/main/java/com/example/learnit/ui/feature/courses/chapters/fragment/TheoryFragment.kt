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
import com.example.learnit.data.ApiConstants.ARG_LESSON_ID
import com.example.learnit.databinding.FragmentTheoryBinding
import com.example.learnit.ui.feature.courses.chapters.viewModel.TheoryViewModel
import kotlinx.coroutines.launch

class TheoryFragment : Fragment() {
    private val viewModel: TheoryViewModel by viewModels()
    private lateinit var binding: FragmentTheoryBinding

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
        val lessonId = arguments?.getInt(ARG_LESSON_ID, -1) ?: -1
        Log.d(TAG, "Lesson id: $lessonId")
        viewModel.loadLessonContents(lessonId)
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
                        }

                        is TheoryViewModel.TheoryPageState.Failure -> {
                            Log.e(TAG, "Error loading lesson contents: ${state.throwable}")
                        }

                    }
                }
            }
        }
    }
}