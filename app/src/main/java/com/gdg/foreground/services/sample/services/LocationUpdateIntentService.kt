package com.gdg.foreground.services.sample.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.gdg.foreground.services.sample.CloudFunctions
import com.gdg.foreground.services.sample.MainActivity
import com.gdg.foreground.services.sample.R
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationResult
import timber.log.Timber


/**
 * Created by Adrian
 */
class LocationUpdateIntentService : Service() {


    override fun onBind(intent: Intent?): IBinder? {
        return null // You must always implement this method; however, if you don't want to allow binding, you should return null.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        createNotificationForegroundService()

        if (intent != null) {
            if (LocationAvailability.hasLocationAvailability(intent)) {
                Timber.d("isLocationAvailable = ${LocationAvailability.extractLocationAvailability(intent).isLocationAvailable}")
            }

            if (LocationResult.hasResult(intent)) {
                val result: LocationResult = LocationResult.extractResult(intent)
                Timber.d("LocationUpdatService lastLocation = ${result.lastLocation}, locations = ${result.locations}")

                CloudFunctions.updateLocation(result.lastLocation)

            }
        }

        return super.onStartCommand(intent, flags, startId)
    }


    private fun createNotificationForegroundService() {

        val notificationManager: NotificationManager? = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel("Location Online", "You are online", NotificationManager.IMPORTANCE_LOW)
            notificationManager?.createNotificationChannel(notificationChannel)
        }

        // What happens if you click on the notification
        val resultIntent = Intent(this, MainActivity::class.java)

        // Need a pending intent here because Notification manager is a foreign application
        // and it therefore needs access to your application's permissions to execute the intent that opens your MainActivity
        val resultPendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )


            // let calls the specified function block with this value as its argument and returns its result.
            notificationManager?.let {
            val notification = NotificationCompat.Builder(this, "Location Online")
                .setContentTitle("This isn't suspicious at all")
                .setContentText("You have absolutely nothing to worry about")
                .setContentIntent(resultPendingIntent)
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setPriority(NotificationCompat.PRIORITY_LOW) // Equal to or greater than or the system will call out the app's behavior in the notification drawer's bottom section.
                .setShowWhen(false) // Hide the last update time
                .setOngoing(true) // Hide the time of the notification
                .build()

            this.startForeground(1337, notification) // Using the same notification id ensures that the notification doesn't recreate itself
        } ?: this.stopSelf()

    }
}