package com.example.learnit.ui.feature.login.fragment

import android.content.Intent
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
import com.example.learnit.databinding.FragmentLoginBinding
import com.example.learnit.ui.activities.MainActivity
import com.example.learnit.ui.feature.login.model.LoginModel
import com.example.learnit.ui.feature.login.viewModel.LoginViewModel
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    companion object {
        val TAG: String = LoginFragment::class.java.simpleName
    }

    private val viewModel: LoginViewModel by viewModels()

    private var parentBinding: FragmentLoginBinding? = null
    private val binding get() = parentBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        parentBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLogin.setOnClickListener {
            handleLoginClick()
        }

        binding.singUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun handleLoginClick() {
        Log.d(TAG, "observeState started00")
        val userName = binding.editTextUsername.text.toString()
        val userPassword = binding.editTextPassword.text.toString()
        if (userName.isBlank()) {
            binding.require1.text = getString(R.string.required)
        } else if (userPassword.isBlank()) {
            binding.require2.text = getString(R.string.required)
        } else {
            binding.textViewError.text = ""
            val loginForm = LoginModel(userName, userPassword)
            viewModel.loadLoginInfo(loginForm)
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.state.collect { state ->
                        when (state) {
                            is LoginViewModel.LoginPageState.Success -> {
                                val intent = Intent(context, MainActivity::class.java)
                                startActivity(intent)
                            }

                            is LoginViewModel.LoginPageState.Failure -> {
                                Log.e(TAG, "Error loading users: ${state.throwable}")
                                binding.textViewError.text =
                                    getString(R.string.invalid_username_or_password)
                            }

                            is LoginViewModel.LoginPageState.Loading -> {
                                Log.d(TAG, "Loading transfer...")
                            }
                        }
                    }
                }
            }
            binding.editTextUsername.text.clear()
            binding.editTextPassword.text.clear()
            //Ha mar letrejott a StringUtils osztalyunk akkor hasznaljuk onnan az EMPTY_STRING -et
            binding.require1.text = ""
            binding.require2.text = ""
            binding.textViewError.text = ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        parentBinding = null
    }

}