package com.learnitevekri.ui.feature.courses.courses.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.google.android.material.snackbar.Snackbar
import com.learnitevekri.data.courses.chapters.model.EditChapterData
import com.learnitevekri.databinding.FragmentEditChapterBinding
import com.learnitevekri.ui.feature.courses.courses.viewModel.ChaptersViewModel
import kotlinx.coroutines.launch

class EditChapterFragment : Fragment() {
    companion object {
        val TAG: String = EditChapterFragment::class.java.simpleName
    }

    private val viewModel: ChaptersViewModel by viewModels()
    private lateinit var binding: FragmentEditChapterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditChapterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chapterId = arguments?.getInt("chapterId")
        if (chapterId != null) {
            viewModel.loadChapterById(chapterId)
        }

        observeState()
        setupCharacterCount()

        binding.btnSave.setOnClickListener {
            val updatedChapter = EditChapterData(
                chapterName = binding.etChapterName.text.toString(),
                chapterDescription = binding.etChapterDescription.text.toString()
            )
            if (chapterId != null) {
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        viewModel.editChapter(chapterId, updatedChapter)
                        Snackbar.make(
                            requireView(), "Chapter updated successfully", Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()
                    } catch (e: Exception) {
                        Snackbar.make(
                            requireView(), "Failed to update chapter", Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state2.collect { state ->
                    when (state) {
                        is ChaptersViewModel.EditChapterState.Loading -> {
                            Log.d(TAG, "Loading chapter by id...")
                        }

                        is ChaptersViewModel.EditChapterState.Success -> {
                            val course = state.chapterData
                            binding.etChapterName.setText(course.chapterName)
                            binding.etChapterDescription.setText(course.chapterDescription)
                            binding.tvCharCount.text = course.chapterDescription.length.toString()
                        }

                        is ChaptersViewModel.EditChapterState.Updated -> {
                            findNavController().popBackStack()
                        }

                        is ChaptersViewModel.EditChapterState.Failure -> {
                            Log.e(
                                TAG,
                                "Error loading course by id: ${state.throwable}"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setupCharacterCount() {
        binding.etChapterDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val currentLength = it.length
                    binding.tvCharCount.text = "$currentLength/255"
                    if (currentLength > 255) {
                        val trimmedText = it.substring(0, 255)
                        binding.etChapterDescription.setText(trimmedText)
                        binding.etChapterDescription.setSelection(trimmedText.length)
                    }
                }
            }
        })
    }
}