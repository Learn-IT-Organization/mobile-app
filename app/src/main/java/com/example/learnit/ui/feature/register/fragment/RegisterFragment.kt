package com.example.learnit.ui.feature.register.fragment

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
import com.example.learnit.databinding.FragmentRegisterBinding
import com.example.learnit.ui.feature.register.model.RegistrationModel
import com.example.learnit.ui.feature.register.viewModel.RegisterViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private val viewModel: RegisterViewModel by viewModels()

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    companion object {
        val TAG: String = RegisterFragment::class.java.simpleName
    }

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

        binding.buttonSignUp.setOnClickListener {
            val registrationModel = createRegistrationModelFromUI()

            viewModel.registerUser(registrationModel)
        }

        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        observeState()
    }

    private fun createRegistrationModelFromUI(): RegistrationModel {
        return RegistrationModel(
            firstName = binding.editTextFirstName.text.toString(),
            lastName = binding.editTextLastName.text.toString(),
            userName = binding.editTextUsername.text.toString(),
            userPassword = binding.editTextPassword.text.toString(),
            gender = getSelectedGender(),
            userLevel = binding.spinnerUserLevel.selectedItem.toString()
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
                            Log.d(TAG, "Loading registration...")
                        }

                        is RegisterViewModel.RegisterPageState.Success -> {
                            Log.d(TAG, "User registered successfully")
                            findNavController().navigate(R.id.action_registerFragment_to_homeFragment)

                        }

                        is RegisterViewModel.RegisterPageState.Failure -> {
                            Log.e(TAG, "Error during registration: ${state.throwable}")
                            // Handle failure state (e.g., show an error message)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}