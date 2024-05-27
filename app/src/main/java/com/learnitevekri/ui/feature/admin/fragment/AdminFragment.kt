package com.learnitevekri.ui.feature.admin.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnitevekri.data.user.teacher.model.TeacherRequestData
import com.learnitevekri.databinding.FragmentAdminBinding
import com.learnitevekri.ui.feature.admin.adapter.AdminAdapter
import com.learnitevekri.ui.feature.admin.viewModel.AdminViewModel
import kotlinx.coroutines.launch

class AdminFragment : Fragment() {
    private val viewModel: AdminViewModel by activityViewModels()
    private lateinit var binding: FragmentAdminBinding
    private lateinit var adapter: AdminAdapter

    companion object {
        val TAG: String = AdminFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is AdminViewModel.TeacherRequestsScreenState.Loading -> {
                        Log.d(TAG, "Loading")
                    }

                    is AdminViewModel.TeacherRequestsScreenState.Success -> {
                        adapter = AdminAdapter(state.teacherRequests)
                        binding.recyclerViewTeacherRequests.adapter = adapter
                        Log.d(TAG, "Success")
                    }

                    is AdminViewModel.TeacherRequestsScreenState.Failure -> {
                        Log.d(TAG, "Failure")
                    }
                }
            }
        }

        viewModel.loadTeacherRequests()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewTeacherRequests.layoutManager = LinearLayoutManager(requireContext())
    }
    
}