package com.example.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import java.util.*
import android.app.Notification

import androidx.core.app.NotificationCompat
import android.app.PendingIntent
import android.os.Looper


class RandomNumberService: Service() {
    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    /*private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "ForegroundServiceChannel",
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }*/

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this, "ForegroundServiceChannel")
            .setContentTitle("App Service")
            .setContentText("App Service work in background")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        // Send a notification that service is started
        notification("Service started.")

        // Do a periodic task
        mHandler = Handler(Looper.getMainLooper())
        mRunnable = Runnable { showRandomNumber() }
        mHandler.postDelayed(mRunnable, 5000)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        notification("Service destroyed.")
        mHandler.removeCallbacks(mRunnable)
    }

    // Custom method to do a task
    private fun showRandomNumber() {
        val rand = Random()
        val number = rand.nextInt(100)
        notification("Random Number : $number")
        mHandler.postDelayed(mRunnable, 5000)
    }
}