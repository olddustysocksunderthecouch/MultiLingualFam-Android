package com.gdg.foreground.services.sample

import android.location.Location
import com.google.firebase.functions.FirebaseFunctions


/**
 * Created by Adrian
 */
object CloudFunctionsService {

    fun updateLocation(location: Location) {
        // Create the arguments to the callable function.
        val data = HashMap<String, Any>()
        data["latitude"] = location.latitude
        data["longitude"] = location.longitude

        FirebaseFunctions.getInstance()
                .getHttpsCallable("updateLocation")
                .call(data)
                .continueWith { task ->
                    task.result.data as String
                }
    }
}