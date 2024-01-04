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
        val TAG = LoginFragment::class.java.simpleName
    }

    //A binding-ot csinaljuk meg szepen ahogy beszeltuk
    private var parentBinding: FragmentLoginBinding? = null
    private val binding get() = parentBinding!!

    private val viewModel: LoginViewModel by viewModels()

    //Foloslegesek ezek a valtozok mivel csak egy fuggvenyben hasznaljuk
    private lateinit var userName: String
    private lateinit var userPassword: String

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
            HandleLoginClick()
        }

        binding.singUpButton.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    //a fuggveny nevek mindig kisbetuvel kezdodnek!
    private fun HandleLoginClick() {
        Log.d(TAG, "observeState started00")
        userName = binding.editTextUsername.text.toString()
        userPassword = binding.editTextPassword.text.toString()
        if (userName.isBlank()) {
            //Hardcode-olt String-eket ne hasznaljunk
            binding.require1.text = "Required"
        } else if (userPassword.isBlank()) {
            binding.require2.text = "Required"
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
                                //Hasznaljunk TAG-et
                                Log.e("LoginFragment", "Error loading users: ${state.throwable}")
                                binding.textViewError.text = "Invalid username or password."
                            }

                            is LoginViewModel.LoginPageState.Loading -> {
                                Log.d("LoginFragment", "Loading transfer...")
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