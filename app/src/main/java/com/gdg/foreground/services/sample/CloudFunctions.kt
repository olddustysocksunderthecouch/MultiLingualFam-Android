package com.gdg.foreground.services.sample

import android.location.Location
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.iid.FirebaseInstanceId


/**
 * Created by Adrian
 */
object CloudFunctions {

    fun updateLocation(location: Location) {

        Log.e("updateLocation", location.toString())        // Create the arguments to the callable function.
        val data = HashMap<String, Any>()
        data["latitude"] = location.latitude
        data["longitude"] = location.longitude

        FirebaseFunctions.getInstance()
            .getHttpsCallable("updateLocation")
            .call(data)
            .continueWith { task ->
                task.result?.data as String
            }
    }

    fun addToken(): Task<String> {
        // Create the arguments to the callable function.

        val data = java.util.HashMap<String, Any?>()
        data["token"] = FirebaseInstanceId.getInstance().token

        return FirebaseFunctions.getInstance()
            .getHttpsCallable("addDeviceToken")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then getResult() will throw an Exception which will be
                // propagated down.
                // Log.e("error viewholder_message",task.exception?.viewholder_message)
                task.result?.data as String
            }

    }
}