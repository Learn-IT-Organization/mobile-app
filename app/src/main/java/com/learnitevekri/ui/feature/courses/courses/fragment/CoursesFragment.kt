package com.learnitevekri.ui.feature.courses.courses.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.learnitevekri.R
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.databinding.FragmentCoursesBinding
import com.learnitevekri.ui.activities.MainActivity
import com.learnitevekri.ui.feature.courses.courses.adapter.CoursesAdapter
import com.learnitevekri.ui.feature.courses.courses.viewModel.CoursesViewModel
import kotlinx.coroutines.launch

class CoursesFragment : Fragment() {
    private val viewModel: CoursesViewModel by viewModels()
    private lateinit var binding: FragmentCoursesBinding
    private val courseUserId = SharedPreferences.getUserId()

    companion object {
        val TAG: String = CoursesFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
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
        binding.fabAddCourse.setOnClickListener {
            if (SharedPreferences.getStudent()) {
                findNavController().navigate(R.id.action_coursesFragment_to_teacherRequestFragment)
            } else {
                findNavController().navigate(R.id.action_coursesFragment_to_addNewCourseFragment)
            }
        }
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
                            val adapter = CoursesAdapter(state.courseData,
                                courseUserId.toString(),
                                onEditClicked = { course ->
                                    val bundle = bundleOf("courseId" to course.course_id)
                                    findNavController().navigate(
                                        R.id.action_CoursesFragment_to_EditCourseFragment,
                                        bundle
                                    )
                                },
                                onDeleteClicked = { course ->
                                    showDeleteConfirmationDialog(course.course_id)
                                }
                            )
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

    private fun showDeleteConfirmationDialog(courseId: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Course")
        builder.setMessage("Are you sure you want to delete this course?")
        builder.setPositiveButton("Yes") { _, _ ->
            viewModel.deleteCourse(courseId)
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    override fun onResume() {
        super.onResume()
        refreshCourses()
    }

    private fun refreshCourses() {
        viewModel.loadCourses()
    }
}