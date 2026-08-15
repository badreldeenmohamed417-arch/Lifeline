package com.example.lifeline.emergencyMode

import android.content.Context
import android.content.Intent
import android.net.Uri

object PhoneLauncher {

    fun openEmergencyNumber(
        context: Context,
        number: String
    ): Boolean {

        val intent = Intent(
            Intent.ACTION_DIAL,
            Uri.parse("tel:${Uri.encode(number)}")
        )

        return try {
            context.startActivity(intent)
            true
        } catch (_: Exception) {
            false
        }
    }
}