package com.example.lifeline.emergencyMode

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

object LocationHelper {

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        context: Context,
        onResult: (Location?) -> Unit
    ) {
        try {
            val client =
                LocationServices.getFusedLocationProviderClient(context)

            client.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            )
                .addOnSuccessListener { location ->
                    onResult(location)
                }
                .addOnFailureListener {
                    onResult(null)
                }
        } catch (e: Exception) {
            com.example.lifeline.util.ErrorHandler.showUnexpectedError(context)
            onResult(null)
        }
    }
}