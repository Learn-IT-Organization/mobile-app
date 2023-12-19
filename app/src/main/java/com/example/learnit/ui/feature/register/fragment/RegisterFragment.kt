package com.example.learnit.ui.feature.register.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.learnit.R
import com.example.learnit.databinding.FragmentRegisterBinding
import com.example.learnit.ui.activities.MainActivity
import com.example.learnit.ui.feature.register.model.RegistrationModel
import com.example.learnit.ui.feature.register.viewModel.RegisterViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private val viewModel: RegisterViewModel by viewModels()
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonSelectPhoto.setOnClickListener { openGallery() }

        binding.buttonSignUp.setOnClickListener {
            if (validateRegistrationFields()) {
                val registrationModel = createRegistrationModelFromUI()
                viewModel.registerUser(registrationModel)
                observeState()
            }
        }

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun createRegistrationModelFromUI(): RegistrationModel {
        return RegistrationModel(
            firstName = binding.editTextFirstName.text.toString(),
            lastName = binding.editTextLastName.text.toString(),
            userName = binding.editTextUsername.text.toString(),
            userPassword = binding.editTextPassword.text.toString(),
            gender = getSelectedGender(),
            userLevel = binding.spinnerUserLevel.selectedItem.toString(),
            userPhoto = "Base64EncodedStringHere",
            streak = 0
        )
    }

    private fun getSelectedGender(): String {
        return when (binding.genderRadioGroup.checkedRadioButtonId) {
            R.id.radioButtonMale -> "male"
            R.id.radioButtonFemale -> "female"
            else -> "unknown"
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is RegisterViewModel.RegisterPageState.Loading -> {
                            // Handle loading state
                        }

                        is RegisterViewModel.RegisterPageState.Success -> {
                            handleSuccessState()
                        }

                        is RegisterViewModel.RegisterPageState.Failure -> {
                            handleFailureState(state.throwable)
                        }
                    }
                }
            }
        }
    }

    private fun handleSuccessState() {
        Toast.makeText(
            context,
            getString(R.string.success_registration),
            Toast.LENGTH_SHORT
        ).show()
        clearTextFields()
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }

    private fun handleFailureState(throwable: Throwable) {
        binding.textViewError.text = getString(R.string.user_exists)
    }

    private fun clearTextFields() {
        binding.editTextFirstName.text.clear()
        binding.editTextLastName.text.clear()
        binding.editTextUsername.text.clear()
        binding.editTextPassword.text.clear()
        binding.genderRadioGroup.clearCheck()
        binding.spinnerUserLevel.setSelection(0)
    }

    private fun validateRegistrationFields(): Boolean {
        val firstName = binding.editTextFirstName.text.toString()
        val lastName = binding.editTextLastName.text.toString()
        val userName = binding.editTextUsername.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (firstName.isBlank() || lastName.isBlank() || userName.isBlank() || password.isBlank()) {
            Toast.makeText(
                context,
                getString(R.string.error_empty_fields),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            if (selectedImageUri != null) {
                binding.imageViewProfilePhoto.setImageURI(selectedImageUri)
                viewModel.setPhotoUri(selectedImageUri)
            }
        }
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}