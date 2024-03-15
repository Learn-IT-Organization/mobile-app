package com.learnitevekri.ui.feature.splash.fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.learnitevekri.R
import com.learnitevekri.databinding.FragmentSplashBinding
import com.learnitevekri.ui.activities.MainActivity
import com.learnitevekri.ui.feature.splash.SplashNavigationListener
import com.learnitevekri.ui.feature.splash.viewModel.SplashViewModel

class SplashFragment : Fragment(), SplashNavigationListener {

    private val viewModel: SplashViewModel by viewModels()
    private lateinit var binding: FragmentSplashBinding

    companion object {
        val TAG: String = SplashFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)

        val topAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_animation)
        val scaleUpAnimation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up_animation)

        val splashImage = binding.splashImage
        val splashTitle = binding.splashTitle

        splashImage.startAnimation(topAnim)
        splashImage.startAnimation(scaleUpAnimation)
        splashTitle.startAnimation(bottomAnim)

        viewModel.setNavigationListener(this)

        if (isNetworkAvailable()) {
            checkTheServer()
        } else {
            navigateToNoInternetFragment()
            Log.d(TAG, "No internet connection")
        }
        return binding.root
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun checkTheServer() {
        viewModel.checkTheServer()
    }

    override fun navigateToNoConnectionFragment() {
        findNavController().navigate(R.id.action_SplashFragment_to_NoConnectionFragment)
    }

    override fun navigateToErrorFragment() {
        findNavController().navigate(R.id.action_SplashFragment_to_ServerErrorFragment)
    }

    override fun navigateToNoInternetFragment() {
        findNavController().navigate(R.id.action_SplashFragment_to_NoInternetFragment)
    }

    override fun initSplashScreen() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d(TAG, "FCM token: $token")
        })

        Handler().postDelayed({
            if (isAdded && !isDetached) {
                if (viewModel.verifyTokenValid()) {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    findNavController().navigate(R.id.action_SplashFragment_to_LoginFragment)
                }
            }
        }, 3000)
    }
}