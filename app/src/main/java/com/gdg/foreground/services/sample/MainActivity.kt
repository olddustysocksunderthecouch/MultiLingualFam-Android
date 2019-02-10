package com.gdg.foreground.services.sample

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        start_button.setOnClickListener {
            startFusedLocationUpdates(googleApiClientToggleFused, locIntent)
        }

        stop_button.setOnClickListener {
            stopFusedLocationUpdates(googleApiClientToggleFused, locIntent)
            val intent = Intent(applicationContext, LocationUpdateIntentService::class.java)
            applicationContext.stopService(intent)
        }
    }

    private val googleApiClientToggleFused: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val locIntent: PendingIntent by lazy {
        Timber.e("Intent starting Location Service: locIntent")
        val intent = Intent(applicationContext, LocationUpdateIntentService::class.java)
        PendingIntent.getService(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    private fun startFusedLocationUpdates(fusedLocationClient: FusedLocationProviderClient, locIntent: PendingIntent) {
        fusedLocationClient.requestLocationUpdates(locationRequest, locIntent)
    }

    private fun stopFusedLocationUpdates(fusedLocationClient: FusedLocationProviderClient, locIntent: PendingIntent) {
        fusedLocationClient.removeLocationUpdates(locIntent)
    }

    private val locationRequest: LocationRequest
        get() {
            val locReq = LocationRequest()
            locReq.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locReq.interval = 15000 // millis
            locReq.smallestDisplacement = 100.0f // meters

            return locReq
        }
}
