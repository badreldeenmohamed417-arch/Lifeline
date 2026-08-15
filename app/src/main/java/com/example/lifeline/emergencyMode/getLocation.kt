package com.example.lifeline.emergencyMode

import android.annotation.SuppressLint
import android.content.Context

import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

sealed class LocationResult {

    data class Success(
        val latitude: Double,
        val longitude: Double
    ) : LocationResult()

    data object Unavailable : LocationResult()
}

object LocationHelper {

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        context: Context,
        onResult: (LocationResult) -> Unit
    ) {
        val client =
            LocationServices.getFusedLocationProviderClient(context)

        client.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        )
            .addOnSuccessListener { location ->

                if (location == null) {
                    onResult(
                        LocationResult.Unavailable
                    )
                } else {
                    onResult(
                        LocationResult.Success(
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                    )
                }
            }
            .addOnFailureListener {
                onResult(
                    LocationResult.Unavailable
                )
            }
    }
}