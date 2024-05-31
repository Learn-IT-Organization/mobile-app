package com.learnitevekri.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.learnitevekri.databinding.FragmentAddQuestionMatchingBinding

class AddQuestionMatching: Fragment() {

    private lateinit var binding: FragmentAddQuestionMatchingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddQuestionMatchingBinding.inflate(inflater, container, false)
        return binding.root
    }
}