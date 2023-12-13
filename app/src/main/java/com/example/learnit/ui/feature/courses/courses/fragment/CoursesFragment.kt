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
import com.example.learnit.databinding.FragmentCoursesBinding
import com.example.learnit.ui.feature.courses.courses.adapter.CoursesAdapter
import com.example.learnit.ui.feature.courses.courses.viewModel.CoursesViewModel
import kotlinx.coroutines.launch

class CoursesFragment : Fragment() {
    private val viewModel: CoursesViewModel by viewModels()
    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!

    companion object {
        val TAG: String = CoursesFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is CoursesViewModel.CoursesPageState.Loading -> {
                            Log.d(TAG, "Loading courses...")
                        }

                        is CoursesViewModel.CoursesPageState.Success -> {
                            Log.d(TAG, "Courses loaded")
                            val adapter = CoursesAdapter(state.courseData)
                            binding.coursesRecycleView.adapter = adapter
                        }

                        is CoursesViewModel.CoursesPageState.Failure -> {
                            Log.e(TAG, "Error loading courses: ${state.throwable}")
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