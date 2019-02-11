package com.gdg.foreground.services.sample

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private var mAlertDialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start_button.setOnClickListener {
            permissionCheck()
        }

        stop_button.setOnClickListener {
            stopFusedLocationUpdates(googleApiClientToggleFused, locIntent)
            val intent = Intent(applicationContext, LocationUpdateIntentService::class.java)
            applicationContext.stopService(intent)
        }
    }

    private fun permissionCheck(){
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showAlertDialog(
                    DialogInterface.OnClickListener { _, _ ->
                    ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 0)
                    },
                    null)

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 0)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            startFusedLocationUpdates(googleApiClientToggleFused, locIntent)

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


    @SuppressLint("MissingPermission")
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            0 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do your work....
                    startFusedLocationUpdates(googleApiClientToggleFused, locIntent)
                } else {
                    // permission denied
                    // Disable the functionality that depends on this permission.
                }
                return
            }
            // other 'case' statements for other permissions
        }
    }

    private fun showAlertDialog(onPositiveButtonClickListener: DialogInterface.OnClickListener?,
                                onNegativeButtonClickListener: DialogInterface.OnClickListener?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Can we track you maybe?")
        builder.setMessage("So this is awkward but we want to watch your every move")
        builder.setPositiveButton("SURE", onPositiveButtonClickListener)
        builder.setNegativeButton("PERHAPS LATER", onNegativeButtonClickListener)
        mAlertDialog = builder.show()
    }
}
