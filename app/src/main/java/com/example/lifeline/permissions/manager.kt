package com.example.lifeline.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher

object LifelinePermissions {

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

    fun hasCallLogPermission(context: Context): Boolean {
        return context.checkSelfPermission(
            Manifest.permission.READ_CALL_LOG
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestCallLogPermission(
        launcher: ManagedActivityResultLauncher<String, Boolean>
    ) {
        launcher.launch(Manifest.permission.READ_CALL_LOG)
    }
}