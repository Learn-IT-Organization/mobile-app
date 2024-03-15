package com.learnitevekri.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.learnitevekri.R
import com.learnitevekri.databinding.ActivityMainBinding
import com.learnitevekri.ui.App
import java.net.ConnectException
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    fun errorHandling(error: Throwable?) {
        val navController = this.findNavController(R.id.nav_host_fragment)
        if (error is ConnectException) {
            navController.navigate(R.id.noInternetFragment)
        }
        if (error is SocketTimeoutException) {
            navController.navigate(R.id.serverErrorFragment)
        }
    }

    fun hideBottomNavigationView() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNavigationView() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!areNotificationsEnabled()) {
            showNotificationPermissionDialog()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = this.findNavController(R.id.nav_host_fragment)
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

        com.learnitevekri.data.MyFirebaseMessagingService.notificationLiveData2.observe(this) {
            if (it) {
                bottomNavigationView.menu.findItem(R.id.notificationsFragment)
                    .setIcon(R.drawable.nav_notifications_active)
                Log.d("MainActivity", "Notification received")
            } else {
                bottomNavigationView.menu.findItem(R.id.notificationsFragment)
                    .setIcon(R.drawable.nav_notifications)
            }
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.coursesFragment -> {
                    navController.navigate(R.id.coursesFragment)
                    true
                }

                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                    true
                }

                R.id.notificationsFragment -> {
                    navController.navigate(R.id.notificationsFragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun areNotificationsEnabled(): Boolean {
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        return notificationManagerCompat.areNotificationsEnabled()
    }

    private fun navigateToAppSettings() {
        val intent: Intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        } else {
            intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
        }
        startActivity(intent)
    }

    private fun showNotificationPermissionDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Notification Permission Required")
        alertDialogBuilder.setMessage("To receive notifications, please enable notification permission in the app settings.")
        alertDialogBuilder.setPositiveButton("Go to Settings") { _, _ ->
            navigateToAppSettings()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        App.instance.setCurrentActivity(this)
    }

    override fun onPause() {
        super.onPause()
        App.instance.setCurrentActivity(null)
    }
}

