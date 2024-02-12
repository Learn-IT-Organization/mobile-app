package com.example.learnit.ui.feature.register.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.learnit.R
import com.example.learnit.data.user.register.model.RegistrationData
import com.example.learnit.databinding.FragmentRegisterBinding
import com.example.learnit.ui.feature.register.viewModel.RegisterViewModel
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: FragmentRegisterBinding

    companion object {
        val TAG: String = RegisterFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupListeners() {

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createRegistrationModelFromUI(): RegistrationData {
        val firstName = binding.editTextFirstName.text.toString()
        val lastName = binding.editTextLastName.text.toString()
        val userName = binding.editTextUsername.text.toString()
        val password = binding.editTextPassword.text.toString()
        val gender = getSelectedGender()
        val userLevel = binding.spinnerUserLevel.selectedItem.toString()
        val streak = 0

        return RegistrationData(
            first_name = firstName,
            last_name = lastName,
            user_name = userName,
            user_password = password,
            gender = gender,
            user_level = userLevel,
            streak = streak
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
                        }

                        is RegisterViewModel.RegisterPageState.Success -> {
                            handleSuccessState()
                        }

                        is RegisterViewModel.RegisterPageState.Failure -> {
                            handleFailureState()
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
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    private fun handleFailureState() {
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

}