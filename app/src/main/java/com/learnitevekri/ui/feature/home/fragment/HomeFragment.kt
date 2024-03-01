package com.learnitevekri.ui.feature.home.fragment

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.learnitevekri.R
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.user.login.model.LoggedUserData
import com.learnitevekri.databinding.FragmentHomeBinding
import com.learnitevekri.ui.feature.home.adapter.MyCoursesAdapter
import com.learnitevekri.ui.feature.home.viewModel.HomeViewModel
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

        viewModel.loadAndLogUsers()

        binding.imageViewProfilePhoto.setOnClickListener {
            openGalleryForImage()
        }

        binding.buttonStartLearning.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_courses)
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
            viewModel.state.collect { state ->
                when (state) {
                    is HomeViewModel.UserPageState.Loading -> {
                        Log.d(TAG, "Loading users...")
                    }

                    is HomeViewModel.UserPageState.Success -> {
                        val loggedUserId = SharedPreferences.getUserId()
                        val loggedUser = viewModel.getUserById(loggedUserId)
                        updateProfileUi(loggedUser!!)
                        binding.myCoursesRecycleView.adapter =
                            MyCoursesAdapter(state.courseData)
                        if (state.courseData.isEmpty()) {
                            binding.textViewContinueLearning.visibility = View.GONE
                            binding.textViewNoCourse.visibility = View.VISIBLE
                            binding.imageViewNoCourse.visibility = View.VISIBLE
                            binding.buttonStartLearning.visibility = View.VISIBLE
                        } else {
                            binding.textViewContinueLearning.visibility = View.VISIBLE
                            binding.textViewNoCourse.visibility = View.GONE
                            binding.imageViewNoCourse.visibility = View.GONE
                            binding.buttonStartLearning.visibility = View.GONE
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

        viewModel.getUserImagePath()

        binding.imageViewProfilePhoto.setOnClickListener {
            openGalleryForImage()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateProfileUi(loggedUser: LoggedUserData) {
        binding.textViewUsername.text = loggedUser.userName
        binding.textViewName.text = loggedUser.firstName + " ! "
        binding.textViewStreaks.text = loggedUser.streak.toString()
        binding.textViewUserLevel.text = loggedUser.userLevel
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