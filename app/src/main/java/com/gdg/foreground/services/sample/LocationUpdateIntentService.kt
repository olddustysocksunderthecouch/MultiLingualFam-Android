package com.gdg.foreground.services.sample

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationResult
import timber.log.Timber


/**
 * Created by Adrian Bunge 12 Dec 2018
 */
class LocationUpdateIntentService : Service() {


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        NotificationUtil.createNotificationForForegroundLocationService(this@LocationUpdateIntentService)

        if (intent != null) {
            if (LocationAvailability.hasLocationAvailability(intent)) {
                Timber.d("isLocationAvailable = ${LocationAvailability.extractLocationAvailability(intent).isLocationAvailable}")
            }

            if (LocationResult.hasResult(intent)) {
                val result: LocationResult = LocationResult.extractResult(intent)
                Timber.d("lastLocation = ${result.lastLocation}, locations = ${result.locations}")
//                locationService.updateCoords(result.lastLocation.latitude, result.lastLocation.longitude)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }
}