package com.example.learnit.ui.feature.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.learnit.R
import com.example.learnit.data.SharedPreferences
import com.example.learnit.databinding.FragmentSettingsBinding
import com.example.learnit.databinding.FragmentSplashBinding

class SettingsFragment : Fragment() {
    private var binding: FragmentSettingsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding!!.logoutButton.setOnClickListener {
            SharedPreferences.clearUserData()
            findNavController().navigate(R.id.action_SettingsFragment_to_LoginFragment)
        }
        return binding!!.root
    }
}