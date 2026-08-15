package com.example.lifeline.emergencyMode

import android.content.Context
import android.telephony.SmsManager
import com.example.lifeline.checkNumbers.SavedContact

sealed class SmsResult {

    data object Success : SmsResult()

    data class Failed(
        val failedNumbers: List<String>
    ) : SmsResult()
}

object SmsSender {
    fun sendEmergencySms(
        context: Context,
        contacts: List<SavedContact>,
        message: String,
        onResult: (SmsResult) -> Unit
    ) {
        if (contacts.isEmpty()) {
            onResult(
                SmsResult.Failed(emptyList())
            )
            return
        }

        try {
            val smsManager =
                context.getSystemService(SmsManager::class.java)

            val failedNumbers = mutableListOf<String>()

            contacts.forEach { contact ->

                try {
                    smsManager.sendTextMessage(
                        contact.number,
                        null,
                        message,
                        null,
                        null
                    )
                } catch (_: Exception) {
                    failedNumbers.add(contact.number)
                }
            }

            if (failedNumbers.isEmpty()) {
                onResult(SmsResult.Success)
            } else {
                onResult(
                    SmsResult.Failed(failedNumbers)
                )
            }

        } catch (_: Exception) {
            com.example.lifeline.util.ErrorHandler.showUnexpectedError(context)
            onResult(
                SmsResult.Failed(
                    contacts.map { it.number }
                )
            )
        }
    }
}