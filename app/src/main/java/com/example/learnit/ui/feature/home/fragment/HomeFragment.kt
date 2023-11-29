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
import com.example.learnit.R
import com.example.learnit.ui.feature.home.viewmodel.HomeFragmentViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewModel: HomeFragmentViewModel by viewModels()

    companion object {
        val TAG: String = HomeFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
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
                        is HomeFragmentViewModel.UserPageState.Loading -> {
                            Log.d(TAG, "Loading users...")
                        }

                        is HomeFragmentViewModel.UserPageState.Success -> {
                            Log.d(TAG, "Users loaded")
                        }

                        is HomeFragmentViewModel.UserPageState.Failure -> {
                            Log.e(TAG, "Error loading users: ${state.throwable}")
                        }

                    }
                }
            }
        }
    }
}