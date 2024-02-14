package com.example.learnit.ui.feature.home.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.example.learnit.ui.feature.home.adapter.MyCoursesAdapter
import com.example.learnit.ui.feature.home.viewModel.HomeViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    companion object {
        val TAG: String = HomeFragment::class.java.simpleName
    }

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeState()

        binding.imageViewProfilePhoto.setOnClickListener {
            openGalleryForImage()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            if (selectedImageUri != null) {
                viewModel.setUserImagePath(selectedImageUri.toString())
                displayProfilePicture(selectedImageUri.toString())
            }
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
                            updateProfileUi()
                            binding.myCoursesRecycleView.adapter =
                                MyCoursesAdapter(state.courseData)
                            if (state.courseData.isEmpty()) {
                                binding.textViewContinueLearning.visibility = View.GONE
                            } else {
                                binding.textViewContinueLearning.visibility = View.VISIBLE
                            }
                            Log.d(TAG, "Users loaded")
                        }

                        is HomeViewModel.UserPageState.ImagePathSuccess -> {
                            displayProfilePicture(state.imagePath)
                        }

                        is HomeViewModel.UserPageState.Failure -> {
                            Log.e(TAG, "Error loading users: ${state.throwable}")
                        }
                    }

                }

            }

        }

        viewModel.getUserImagePath()

        binding.imageViewProfilePhoto.setOnClickListener {
            openGalleryForImage()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateProfileUi() {
        val loggedUserId = SharedPreferences.getUserId()
        val loggedUser = viewModel.getUserById(loggedUserId)
        binding.textViewUsername.text = loggedUser?.userName
        binding.textViewName.text = loggedUser?.firstName + " ! "
        binding.textViewStreaks.text = loggedUser?.streak.toString()
        binding.textViewUserLevel.text = loggedUser?.userLevel
    }

    private fun displayProfilePicture(imagePath: String?) {
        if (!imagePath.isNullOrEmpty()) {
            Glide.with(this)
                .load(imagePath)
                .override(100, 100)
                .into(binding.imageViewProfilePhoto)

        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

}