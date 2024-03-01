package com.learnitevekri.ui.feature.login.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.learnitevekri.R
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.user.login.model.ForgotPasswordData
import com.learnitevekri.data.user.login.model.ResetCodeData
import com.learnitevekri.databinding.DialogForgotPasswordBinding
import com.learnitevekri.databinding.DialogResetPasswordBinding
import com.learnitevekri.databinding.FragmentLoginBinding
import com.learnitevekri.ui.activities.MainActivity
import com.learnitevekri.ui.feature.login.model.LoginModel
import com.learnitevekri.ui.feature.login.viewModel.LoginViewModel
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

        val savedUsername = SharedPreferences.getRememberedUsername(requireContext())
        val savedPassword = SharedPreferences.getRememberedPassword(requireContext())

        binding.editTextUsername.setText(savedUsername)
        binding.editTextPassword.setText(savedPassword)

        binding.buttonLogin.setOnClickListener {
            handleLoginClick()
        }

        binding.singUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.forgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }

        observeResetCodeState()

        val imageViewShowPassword = binding.imageViewShowPassword
        val editTextPassword = binding.editTextPassword

        imageViewShowPassword.setOnClickListener {
            val isPasswordVisible =
                editTextPassword.transformationMethod is PasswordTransformationMethod

            if (isPasswordVisible) {
                editTextPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                imageViewShowPassword.setImageResource(R.drawable.ic_eye_off)
            } else {
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                imageViewShowPassword.setImageResource(R.drawable.ic_eye)
            }

            editTextPassword.setSelection(editTextPassword.text.length)
        }
    }

    private fun handleLoginClick() {
        val userName = binding.editTextUsername.text.toString()
        val userPassword = binding.editTextPassword.text.toString()

        if (binding.rememberMeCheckbox.isChecked) {
            SharedPreferences.saveRememberedCredentials(requireContext(), userName, userPassword)
        } else {
            SharedPreferences.clearRememberedCredentials(requireContext())
        }

        if (userName.isBlank()) {
            binding.require1.text = getString(R.string.required)
        } else if (userPassword.isBlank()) {
            binding.require2.text = getString(R.string.required)
        } else {
            binding.textViewError.text = ""
            val loginForm = LoginModel(userName, userPassword)
            viewModel.loadLoginInfo(loginForm)
            lifecycleScope.launch {
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
            binding.editTextUsername.text.clear()
            binding.editTextPassword.text.clear()
            binding.require1.text = ""
            binding.require2.text = ""
            binding.textViewError.text = ""
        }
    }

    private fun showForgotPasswordDialog() {
        val binding = DialogForgotPasswordBinding.inflate(layoutInflater)

        val editTextUsername = binding.editTextUsername
        val editTextEmail = binding.editTextEmail

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(binding.root)

        val alertDialog = alertDialogBuilder.create()

        binding.buttonCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        binding.buttonSendEmail.setOnClickListener {
            val username = editTextUsername.text.toString()
            val email = editTextEmail.text.toString()
            if (username.isNotBlank() || email.isNotBlank()) {
                viewModel.sendPasswordReset(ForgotPasswordData(username, email))
            } else {
                Toast.makeText(requireContext(), "Enter your credentials", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        alertDialog.show()
    }

    private fun observeResetCodeState() {
        lifecycleScope.launch {
            viewModel.resetCodeState.collect { state ->
                when (state) {
                    is LoginViewModel.ResetCodeState.Loading -> {
                        Toast.makeText(
                            requireContext(),
                            "Sending reset code...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is LoginViewModel.ResetCodeState.Success -> {
                        showResetPasswordDialog(state.forgotPasswordData.userName)
                    }

                    is LoginViewModel.ResetCodeState.PasswordChangedSuccess -> {
                        Toast.makeText(
                            requireContext(),
                            "Password changed successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is LoginViewModel.ResetCodeState.Failure -> {

                    }
                }
            }
        }
    }

    private fun showResetPasswordDialog(userName: String) {
        val inflater = layoutInflater
        val binding = DialogResetPasswordBinding.inflate(inflater)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(binding.root)

        val alertDialog = alertDialogBuilder.create()

        val editTextNewPassword = binding.editTextNewPassword
        val imageViewShowPasswordNew = binding.imageViewShowPassword

        imageViewShowPasswordNew.setOnClickListener {
            togglePasswordVisibility(editTextNewPassword, imageViewShowPasswordNew)
        }

        val editTextNewPasswordConfirm = binding.editTextNewPasswordConfirm
        val imageViewShowPasswordConfirm = binding.imageViewShowPassword2

        imageViewShowPasswordConfirm.setOnClickListener {
            togglePasswordVisibility(editTextNewPasswordConfirm, imageViewShowPasswordConfirm)
        }

        binding.buttonResetPassword.setOnClickListener {
            val newPassword = editTextNewPassword.text.toString()
            val confirmPassword = editTextNewPasswordConfirm.text.toString()
            val resetCode = binding.editTextResetCode.text.toString()

            if (newPassword.isNotBlank() && confirmPassword.isNotBlank() && newPassword == confirmPassword && resetCode.isNotBlank()) {
                Log.d(TAG, "showResetPasswordDialog: $userName, $resetCode, $newPassword")
                viewModel.changePasswordWithResetCode(
                    ResetCodeData(
                        userName,
                        resetCode.toLong(),
                        newPassword
                    )
                )
            } else {
                Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        alertDialog.show()
    }

    private fun togglePasswordVisibility(editText: EditText, imageView: ImageView) {
        val selection = editText.selectionStart

        if (editText.transformationMethod == null) {
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            imageView.setImageResource(R.drawable.ic_eye)
        } else {
            editText.transformationMethod = null
            imageView.setImageResource(R.drawable.ic_eye_off)
        }

        editText.setSelection(selection)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        parentBinding = null
    }

}