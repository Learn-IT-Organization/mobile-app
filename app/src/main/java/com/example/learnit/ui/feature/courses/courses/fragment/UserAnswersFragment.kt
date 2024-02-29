package com.example.learnit.ui.feature.courses.courses.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnit.R
import com.example.learnit.data.ApiConstants
import com.example.learnit.data.ApiConstants.ARG_LESSON_ID
import com.example.learnit.data.ApiConstants.ARG_LESSON_SCORE
import com.example.learnit.data.courses.lessons.model.UserAnswersData
import com.example.learnit.databinding.FragmentUserAnswersBinding
import com.example.learnit.ui.feature.courses.courses.adapter.UserAnswerAdapter
import com.example.learnit.ui.feature.courses.courses.viewModel.UserAnswersViewModel
import kotlinx.coroutines.launch

class UserAnswersFragment : Fragment() {
    companion object {
        val TAG: String = UserAnswersFragment::class.java.simpleName
    }

    private lateinit var binding: FragmentUserAnswersBinding
    private val viewModel: UserAnswersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserAnswersBinding.inflate(inflater, container, false)

        binding.imageViewBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val lessonId = arguments?.getInt(ARG_LESSON_ID, -1) ?: -1

        viewModel.loadUserAnswers(lessonId)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLessonResult()
    }

    private fun observeLessonResult() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is UserAnswersViewModel.UserAnswersScreenState.Loading -> {
                        Log.d(TAG, "Loading user answers")
                    }

                    is UserAnswersViewModel.UserAnswersScreenState.Success -> {
                        Log.d(TAG, "User answers: ${state.userAnswersData}")
                        displayUserAnswers(state.userAnswersData)
                    }

                    is UserAnswersViewModel.UserAnswersScreenState.Failure -> {
                        Log.e(TAG, "Error fetching user answers: ${state.throwable.message}")
                    }
                }
            }
        }
    }

    private fun displayUserAnswers(userAnswers: List<UserAnswersData>) {
        val adapter = UserAnswerAdapter(userAnswers)
        binding.answersRecyclerView.adapter = adapter
        binding.answersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}