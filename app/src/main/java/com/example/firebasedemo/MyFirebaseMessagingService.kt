package com.example.firebasedemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


        // Get Data from Body
        val id = remoteMessage.data["id"]
        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["body"]
        if(id != null){
            Log.d("Logger", "id = $id")
        }else{
            Log.d("Logger", "Message data payload")
        }

        // show Notification
        // When click notification
        val intent = Intent(this, AlertDetailsAct::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Build NotificationCompat
        val channelId = "channelId"
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Create NotificationChannel & NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(101, builder.build())
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("Logger onNewToken", token)
    }
}