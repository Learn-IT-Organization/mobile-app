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

    private var _binding: FragmentLessonsBinding? = null
    private val binding get() = _binding!!

    companion object {
        val TAG: String = LessonsFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLessonsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "Lessons fragment")
        observeState()
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
        _binding = null
    }


}