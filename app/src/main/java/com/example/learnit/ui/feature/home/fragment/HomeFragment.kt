package com.example.learnit.ui.feature.home.fragment

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
import androidx.navigation.fragment.findNavController
import com.example.learnit.R
import com.example.learnit.databinding.FragmentHomeBinding
import com.example.learnit.ui.feature.home.viewModel.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()


    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    companion object {
        val TAG: String = HomeFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.goToChapters.setOnClickListener {
            Log.d("HomeFragment", "Button clicked")
            findNavController().navigate(
                R.id.action_homeFragment_to_chaptersFragment
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        binding.button.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_lessonsFragment)
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is HomeViewModel.UserPageState.Loading -> {
                            Log.d(TAG, "Loading users...")
                        }

                        is HomeViewModel.UserPageState.Success -> {
                            Log.d(TAG, "Users loaded")
                        }

                        is HomeViewModel.UserPageState.Failure -> {
                            Log.e(TAG, "Error loading users: ${state.throwable}")
                        }

                    }
                }
            }
        }
    }
}