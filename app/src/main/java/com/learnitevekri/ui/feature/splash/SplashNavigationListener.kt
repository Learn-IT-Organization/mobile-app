package com.learnitevekri.ui.feature.splash

interface SplashNavigationListener{
    fun navigateToNoConnectionFragment()
    fun navigateToErrorFragment()
    fun initSplashScreen()
    fun navigateToNoInternetFragment()

}