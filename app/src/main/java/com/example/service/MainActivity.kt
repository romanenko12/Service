package com.example.service

import android.app.ActivityManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.app.NotificationManager

import android.graphics.BitmapFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1234)

        val startButton: Button = findViewById(R.id.startButton)
        val stopButton: Button = findViewById(R.id.stopButton)
        val statButton: Button = findViewById(R.id.statButton)

        val serviceClass = RandomNumberService::class.java

        val intent = Intent(this, serviceClass)

        startButton.setOnClickListener {
            // If the service is not running then start it
            if (!isServiceRunning(serviceClass)) {
                startForegroundService(intent)
            } else {
                notification("Service already running.")
            }
        }

        stopButton.setOnClickListener {
            if (isServiceRunning(serviceClass)) {
                stopService(intent)
            } else {
                notification("Service already stopped.")
            }
        }

        statButton.setOnClickListener {
            if (isServiceRunning(serviceClass)) {
                notification("Service is running.")
            } else {
                notification("Service is stopped.")
            }
        }
    }

    // Custom method to determine whether a service is running
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        // Loop through the running services
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                // If the service is running then return true
                return true
            }
        }
        return false
    }
}

// Extension function to show notification message
fun Context.notification(message: String) {
    val channelId = "i.apps.notifications"

    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val intent = Intent(this, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    val builder = Notification.Builder(this, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        //.setContentTitle("Title")
        .setContentText(message)
        .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
        .setContentIntent(pendingIntent)
    notificationManager.notify(1234, builder.build())
}