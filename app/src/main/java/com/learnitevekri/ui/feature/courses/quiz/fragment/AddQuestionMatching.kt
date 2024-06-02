package com.learnitevekri.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnitevekri.databinding.FragmentAddQuestionMatchingBinding
import com.learnitevekri.ui.feature.courses.quiz.adapter.MatchingPairsAdapter

class AddQuestionMatching : Fragment() {

    private lateinit var binding: FragmentAddQuestionMatchingBinding
    private lateinit var adapter: MatchingPairsAdapter
    private val pairsList = mutableListOf<Pair<String, String>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddQuestionMatchingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MatchingPairsAdapter(pairsList)
        binding.rvMatchingPairs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMatchingPairs.adapter = adapter

        binding.btnAddPair.setOnClickListener {
            pairsList.add(Pair("", ""))
            adapter.notifyItemInserted(pairsList.size - 1)
        }
    }
}