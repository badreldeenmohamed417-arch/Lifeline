package com.example.lifeline.emergencyMode.phone

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat
import com.example.lifeline.checkNumbers.SavedContact

object PhoneLauncher {

    fun callContact(
        context: Context,
        contact: SavedContact
    ): Boolean {

        return callNumber(
            context = context,
            number = contact.number
        )
    }

    fun callNumber(
        context: Context,
        number: String
    ): Boolean {

        val cleanNumber = number.trim()

        if (cleanNumber.isBlank()) {
            return false
        }

        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }

        val intent = Intent(
            Intent.ACTION_CALL,
            Uri.parse("tel:${Uri.encode(cleanNumber)}")
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        return try {
            context.startActivity(intent)
            true
        } catch (_: SecurityException) {
            false
        } catch (_: Exception) {
            false
        }
    }
}