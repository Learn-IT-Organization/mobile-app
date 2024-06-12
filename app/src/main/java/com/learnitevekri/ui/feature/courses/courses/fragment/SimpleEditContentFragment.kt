package com.learnitevekri.ui.feature.courses.courses.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.learnitevekri.data.courses.lessons.model.EditLessonContentData
import com.learnitevekri.databinding.FragmentSimpleEditContentBinding
import com.learnitevekri.ui.feature.courses.courses.viewModel.TheoryViewModel
import kotlinx.coroutines.launch

class SimpleEditContentFragment : Fragment() {
    private val viewModel: TheoryViewModel by viewModels()
    private lateinit var binding: FragmentSimpleEditContentBinding
    private var contentId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSimpleEditContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentId = arguments?.getInt("content_id", -1) ?: -1
        setupListeners()
        observeEditState()
    }

    private fun setupListeners() {
        binding.saveButton.setOnClickListener {
            val url = binding.urlEditText.text.toString()
            val title = binding.titleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            val editLessonContentData = EditLessonContentData(url, title, description)
            viewModel.editLessonContent(contentId, editLessonContentData)
        }
    }

    private fun observeEditState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.editState.collect { state ->
                    when (state) {
                        is TheoryViewModel.EditPageState.Loading -> {
                            Toast.makeText(
                                requireContext(),
                                "Editing content...",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is TheoryViewModel.EditPageState.Success -> {
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT)
                                .show()
                            parentFragmentManager.popBackStack()
                        }

                        is TheoryViewModel.EditPageState.Failure -> {
                            Toast.makeText(
                                requireContext(),
                                "Error: ${state.throwable.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
            }
        }
    }
}