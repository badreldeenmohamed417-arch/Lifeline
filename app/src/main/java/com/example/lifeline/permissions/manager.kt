package com.example.lifeline.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.app.ActivityCompat

object LifelinePermissions {
    private const val REQUEST_MISSING_PERMISSIONS = 1001

    // =========================
    // Location
    // =========================

    fun hasLocationPermission(
        context: Context
    ): Boolean {
        return context.checkSelfPermission(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(
        launcher: ManagedActivityResultLauncher<String, Boolean>
    ) {
        launcher.launch(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


    // =========================
    // SMS
    // =========================

    fun hasSmsPermission(
        context: Context
    ): Boolean {
        return context.checkSelfPermission(
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestSmsPermission(
        launcher: ManagedActivityResultLauncher<String, Boolean>
    ) {
        launcher.launch(
            Manifest.permission.SEND_SMS
        )
    }


    // =========================
    // Phone
    // =========================

    fun hasPhonePermission(
        context: Context
    ): Boolean {
        return context.checkSelfPermission(
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPhonePermission(
        launcher: ManagedActivityResultLauncher<String, Boolean>
    ) {
        launcher.launch(
            Manifest.permission.CALL_PHONE
        )
    }

    fun hasContactsPermission(context: Context): Boolean {
        return context.checkSelfPermission(
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestContactsPermission(
        launcher: ManagedActivityResultLauncher<String, Boolean>
    ) {
        launcher.launch(Manifest.permission.READ_CONTACTS)
    }


    fun hasNotificationsPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.checkSelfPermission(
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun requestNotificationPermission(
        launcher: ManagedActivityResultLauncher<String, Boolean>
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    fun hasPhoneStatePermission(
        context: Context
    ): Boolean {
        return context.checkSelfPermission(
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPhoneStatePermission(
        launcher: ManagedActivityResultLauncher<String, Boolean>
    ) {
        launcher.launch(
            Manifest.permission.READ_PHONE_STATE
        )
    }

    fun shouldOpenSettings(activity: Activity): Boolean {
        return !hasContactsPermission(activity) &&
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.READ_CONTACTS
                )
    }

    fun requestMissingPermissions(
        activity: Activity
    ) {
        val missingPermissions = mutableListOf<String>()

        // Location
        if (!hasLocationPermission(activity)) {
            missingPermissions.add(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }

        // SMS
        if (!hasSmsPermission(activity)) {
            missingPermissions.add(
                Manifest.permission.SEND_SMS
            )
        }

        // Phone
        if (!hasPhonePermission(activity)) {
            missingPermissions.add(
                Manifest.permission.CALL_PHONE
            )
        }

        // Phone State
        if (!hasPhoneStatePermission(activity)) {
            missingPermissions.add(
                Manifest.permission.READ_PHONE_STATE
            )
        }

        // Contacts
        if (!hasContactsPermission(activity)) {
            missingPermissions.add(
                Manifest.permission.READ_CONTACTS
            )
        }

        // Notifications - Android 13+
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !hasNotificationsPermission(activity)
        ) {
            missingPermissions.add(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        // Request all missing permissions
        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                missingPermissions.toTypedArray(),
                REQUEST_MISSING_PERMISSIONS
            )
        }
    }
}