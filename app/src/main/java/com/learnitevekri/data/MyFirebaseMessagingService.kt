package com.learnitevekri.data

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learnitevekri.ui.activities.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.learnitevekri.R
import com.learnitevekri.domain.login.LoginRepository
import com.learnitevekri.ui.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val repository: LoginRepository = App.instance.getLoginRepository()

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        private const val CHANNEL_ID = "NewChapterChannel"
        private const val NOTIFICATION_ID = 1

        private val notificationLiveData = MutableLiveData<Boolean>()
        val notificationLiveData2: LiveData<Boolean> =
            com.learnitevekri.data.MyFirebaseMessagingService.Companion.notificationLiveData

        fun notificationIsActive() {
            com.learnitevekri.data.MyFirebaseMessagingService.Companion.notificationLiveData.value = false
        }
    }

    override fun onNewToken(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.sendFCMToken(token)
            }
            catch (e: Exception) {
                Log.e(TAG, "Error sending FCM token: $e")
            }
        }
        Log.d(com.learnitevekri.data.MyFirebaseMessagingService.Companion.TAG, "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(com.learnitevekri.data.MyFirebaseMessagingService.Companion.TAG, "Message received: " + remoteMessage.notification?.body)

        if (remoteMessage.data.isNotEmpty()) {
            val isNewChapter = remoteMessage.data["isNewChapter"]

            if (isNewChapter == "true") {
                Log.d(com.learnitevekri.data.MyFirebaseMessagingService.Companion.TAG, "New chapter available")
                showNewChapterNotification()
                com.learnitevekri.data.MyFirebaseMessagingService.Companion.notificationLiveData.postValue(true)
                saveNotificationToSharedPreferences(
                    this,
                    remoteMessage.notification?.body ?: ""
                )
//                clearNotificationsFromSharedPreferences(this)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNewChapterNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                com.learnitevekri.data.MyFirebaseMessagingService.Companion.CHANNEL_ID, "New Chapter Notifications", NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val notification =
            NotificationCompat.Builder(this,
                com.learnitevekri.data.MyFirebaseMessagingService.Companion.CHANNEL_ID
            )
                .setContentTitle("New Chapter Available")
                .setContentText("A new chapter is available for you to learn!")
                .setSmallIcon(R.drawable.nav_notifications)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
        notificationManager.notify(com.learnitevekri.data.MyFirebaseMessagingService.Companion.NOTIFICATION_ID, notification)
    }

    private fun saveNotificationToSharedPreferences(context: Context, notificationBody: String) {
        val sharedPreferences = context.getSharedPreferences("notification_pref", Context.MODE_PRIVATE)

        val notifications = sharedPreferences.getStringSet("notifications", mutableSetOf())?.toMutableList()

        notifications?.add(notificationBody)

        notifications?.sortByDescending { it }

        sharedPreferences.edit().putStringSet("notifications", notifications?.toSet()).apply()
    }


    private fun clearNotificationsFromSharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("notification_pref", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("notifications").apply()
    }
}