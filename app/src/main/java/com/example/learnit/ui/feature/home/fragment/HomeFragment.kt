package com.example.learnit.ui.feature.home.fragment

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import com.example.learnit.data.SharedPreferences
import com.example.learnit.databinding.FragmentHomeBinding
import com.example.learnit.ui.feature.home.viewModel.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    companion object {
        val TAG: String = HomeFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        val loggedUserId = SharedPreferences.getUserId()
        Log.d(TAG, "Logged user id: $loggedUserId")
        viewModel.getUserById(loggedUserId)
        Log.d(TAG, "Logged user: ${viewModel.getUserById(loggedUserId)}")

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
                            updateProfileUi()
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

    @SuppressLint("SetTextI18n")
    private fun updateProfileUi() {
        val loggedUserId = SharedPreferences.getUserId()
        val loggedUser = viewModel.getUserById(loggedUserId)
        Glide.with(this)
            .load(loggedUser?.userPhoto)
            .into(binding.imageViewProfilePhoto)
        binding.textViewUsername.text = loggedUser?.userName
        binding.textViewName.text = loggedUser?.firstName + " " + loggedUser?.lastName
        binding.textViewStreaks.text = loggedUser?.streak.toString()
        binding.textViewGender.text = loggedUser?.gender
        binding.textViewUserLevel.text = loggedUser?.userLevel
    }
}