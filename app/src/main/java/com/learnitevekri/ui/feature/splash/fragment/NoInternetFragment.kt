package com.learnitevekri.ui.feature.splash.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.learnitevekri.databinding.FragmentNoInternetBinding

class NoInternetFragment : Fragment() {
    private lateinit var binding: FragmentNoInternetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoInternetBinding.inflate(inflater, container, false)
        binding.tryAgainButton.setOnClickListener {
            val intent =
                requireActivity().packageManager.getLaunchIntentForPackage(requireActivity().packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent!!)
            requireActivity().finish()
        }
        return binding.root
    }
}