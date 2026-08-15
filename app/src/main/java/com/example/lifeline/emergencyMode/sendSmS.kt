package com.example.lifeline.emergencyMode

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.lifeline.checkNumbers.SavedContact

sealed class SmsResult {
    data object Success : SmsResult()

    data class Failed(
        val failedNumbers: List<String>
    ) : SmsResult()
}

object SmsSender {

    private const val TAG = "LifelineSms"

    fun sendEmergencySms(
        context: Context,
        contacts: List<SavedContact>,
        message: String,
        onResult: (SmsResult) -> Unit
    ) {

        Log.d(TAG, "Starting emergency SMS")

        if (contacts.isEmpty()) {
            Log.e(TAG, "No contacts")
            onResult(SmsResult.Failed(emptyList()))
            return
        }

        // Check permission
        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "SEND_SMS permission NOT granted")

            onResult(
                SmsResult.Failed(
                    contacts.map { it.number }
                )
            )

            return
        }

        try {

            val smsManager =
                SmsManager.getDefault()

            Log.d(TAG, "SmsManager obtained successfully")

            val failedNumbers =
                mutableListOf<String>()

            contacts.forEach { contact ->

                val number =
                    contact.number.trim()

                if (number.isBlank()) {
                    Log.e(
                        TAG,
                        "Empty number for ${contact.name}"
                    )

                    return@forEach
                }

                Log.d(
                    TAG,
                    "Sending SMS to ${contact.name}: $number"
                )

                try {

                    smsManager.sendTextMessage(
                        number,
                        null,
                        message,
                        null,
                        null
                    )

                    Log.d(
                        TAG,
                        "SMS request submitted for $number"
                    )

                } catch (e: Exception) {

                    Log.e(
                        TAG,
                        "Failed to send SMS to $number",
                        e
                    )

                    failedNumbers.add(number)
                }
            }

            if (failedNumbers.isEmpty()) {

                Log.d(
                    TAG,
                    "All SMS requests submitted successfully"
                )

                onResult(
                    SmsResult.Success
                )

            } else {

                Log.e(
                    TAG,
                    "Failed numbers: $failedNumbers"
                )

                onResult(
                    SmsResult.Failed(
                        failedNumbers
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Fatal SMS error",
                e
            )

            onResult(
                SmsResult.Failed(
                    contacts.map {
                        it.number
                    }
                )
            )
        }
    }
}