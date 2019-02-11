package com.gdg.foreground.services.sample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationCompat.PRIORITY_MIN
import android.support.v4.content.ContextCompat

/**
 * Created by Adrian
 */
object NotificationUtil {

    fun createNotificationForForegroundLocationService(service: LocationUpdateIntentService) {

        NotificationCompat.Builder(service, "Driver Online")
        val notificationManager: NotificationManager? = service.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val resultIntent = Intent(service, MainActivity::class.java)

        val resultPendingIntent =
            PendingIntent.getActivity(
                service,
                0,
                resultIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )

        notificationManager?.let {
            val notification = NotificationCompat.Builder(service, "Driver Online")
                .setContentTitle("You're online")
                .setContentText("Go offline to stop location tracking")
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(ContextCompat.getColor(service, R.color.colorPrimary))
                .setPriority(PRIORITY_MIN)
                .setShowWhen(false)
                .setOngoing(true)
                .build()

            service.startForeground(1337, notification) // Using the same notification id ensures that the notification doesn't recreate itself
        } ?: service.stopSelf()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel("Driver Online", "You are online", NotificationManager.IMPORTANCE_LOW)
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }
}