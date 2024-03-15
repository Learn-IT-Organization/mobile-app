package com.learnitevekri.ui.feature.courses.courses.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.learnitevekri.R
import com.learnitevekri.databinding.FragmentCoursesBinding
import com.learnitevekri.ui.activities.MainActivity
import com.learnitevekri.ui.feature.courses.courses.adapter.CoursesAdapter
import com.learnitevekri.ui.feature.courses.courses.viewModel.CoursesViewModel
import kotlinx.coroutines.launch

class CoursesFragment : Fragment() {
    private val viewModel: CoursesViewModel by viewModels()
    private lateinit var binding: FragmentCoursesBinding

    companion object {
        val TAG: String = CoursesFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoursesBinding.inflate(inflater, container, false)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_coursesFragment_to_homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

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
                            (activity as MainActivity?)?.errorHandling(state.throwable)
                        }
                    }
                }
            }
        }
    }
}