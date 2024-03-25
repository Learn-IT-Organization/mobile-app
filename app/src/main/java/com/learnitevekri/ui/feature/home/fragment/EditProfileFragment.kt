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
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.learnitevekri.R
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.user.login.model.EditProfileData
import com.learnitevekri.data.user.login.model.LoggedUserData
import com.learnitevekri.databinding.FragmentEditProfileBinding
import com.learnitevekri.databinding.ProfileUpdatedDialogBinding
import com.learnitevekri.ui.activities.MainActivity
import com.learnitevekri.ui.feature.home.viewModel.EditProfileViewModel
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels()
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
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

        binding.buttonSave.setOnClickListener {
            val editProfileData = createModelFromUI()
            viewModel.editProfile(editProfileData)
        }

        viewModel.profileUpdated.observe(viewLifecycleOwner) { updated ->
            if (updated) {
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val view = ProfileUpdatedDialogBinding.inflate(layoutInflater).root
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()

        view.findViewById<Button>(R.id.okButton).setOnClickListener {
            findNavController().popBackStack()
            dialog.dismiss()
        }
    }

    private fun createModelFromUI(): EditProfileData {
        val firstName = binding.editTextFirstName.text.toString().takeIf { it.isNotBlank() }
        val lastName = binding.editTextLastName.text.toString().takeIf { it.isNotBlank() }
        val userName = binding.editTextUserName.text.toString().takeIf { it.isNotBlank() }
        val password = binding.editTextUserPassword.text.toString().takeIf { it.isNotBlank() }
        val gender = getSelectedGender()
        val userLevel = binding.spinnerUserLevel.selectedItem.toString()

        return EditProfileData(
            firstName = firstName,
            lastName = lastName,
            userName = userName,
            userPassword = password,
            gender = gender,
            userLevel = userLevel
        )
    }

    private fun getSelectedGender(): String {
        return when (binding.genderRadioGroup.checkedRadioButtonId) {
            R.id.radioButtonMale -> "male"
            R.id.radioButtonFemale -> "female"
            else -> "unknown"
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
                    is EditProfileViewModel.UserPageState.Loading -> {
                        Log.d(HomeFragment.TAG, "Loading users...")
                    }

                    is EditProfileViewModel.UserPageState.Success -> {
                        val loggedUserId = SharedPreferences.getUserId()
                        val loggedUser = viewModel.getUserById(loggedUserId)
                        if (loggedUser != null) {
                            updateProfileUi(loggedUser)
                        }
                        Log.d(HomeFragment.TAG, "Users loaded")
                    }

                    is EditProfileViewModel.UserPageState.ImagePathSuccess -> {
                        displayProfilePicture(state.imagePath)
                    }

                    is EditProfileViewModel.UserPageState.Failure -> {
                        Log.e(HomeFragment.TAG, "Error loading users: ${state.throwable}")
                        (activity as MainActivity?)?.errorHandling(state.throwable)
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
        binding.editTextFirstName.hint = loggedUser.firstName
        binding.editTextLastName.hint = loggedUser.lastName
        binding.editTextUserName.hint = loggedUser.userName
        val stars = "*".repeat(10)
        binding.editTextUserPassword.hint = stars

        if (loggedUser.gender == "male") {
            binding.radioButtonMale.isChecked = true
        } else {
            binding.radioButtonFemale.isChecked = true
        }

        val userLevels = resources.getStringArray(R.array.user_level_array)
        val levelIndex = userLevels.indexOf(loggedUser.userLevel)
        binding.spinnerUserLevel.setSelection(levelIndex)
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