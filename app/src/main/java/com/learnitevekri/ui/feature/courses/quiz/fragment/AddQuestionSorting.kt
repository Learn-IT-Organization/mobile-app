package com.learnitevekri.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnitevekri.databinding.FragmentAddQuestionSortingBinding
import com.learnitevekri.ui.feature.courses.quiz.adapter.SortingItemAdapter

class AddQuestionSorting: Fragment() {
    private lateinit var binding: FragmentAddQuestionSortingBinding
    private lateinit var group1ItemsAdapter: SortingItemAdapter
    private lateinit var group2ItemsAdapter: SortingItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddQuestionSortingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        group1ItemsAdapter = SortingItemAdapter(mutableListOf())
        group2ItemsAdapter = SortingItemAdapter(mutableListOf())

        binding.rvGroup1Items.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGroup1Items.adapter = group1ItemsAdapter

        binding.rvGroup2Items.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGroup2Items.adapter = group2ItemsAdapter

        binding.btnAddItemGroup1.setOnClickListener {
            val item = binding.etGroup1Item.text.toString()
            if (item.isNotEmpty()) {
                group1ItemsAdapter.addItem(item)
                binding.etGroup1Item.text.clear()
            }
        }

        binding.btnAddItemGroup2.setOnClickListener {
            val item = binding.etGroup2Item.text.toString()
            if (item.isNotEmpty()) {
                group2ItemsAdapter.addItem(item)
                binding.etGroup2Item.text.clear()
            }
        }
    }
}