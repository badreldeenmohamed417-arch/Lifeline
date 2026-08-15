package com.example.lifeline.emergencyMode

import android.content.Context
import com.example.lifeline.checkNumbers.ContactStorage
import com.example.lifeline.checkNumbers.SavedContact
import com.example.lifeline.emergencyMessage.EmergencyMessageStorage
import com.example.lifeline.emergencyMode.phone.EmergencyManager
import com.example.lifeline.emergencyMode.phone.PhoneLauncher
import com.example.lifeline.emergencyNumber.EmergencyNumberStorage

fun emergencyWorker(
    context: Context,
    location: String,
    contacts: List<SavedContact>,
    message: String,
    number: String
) {
    try {

        val locationMsg = if (location.isNotBlank()) {
            "Location on Maps: https://www.google.com/maps?q=$location"
        } else {
            ""
        }

        val emergencyMessage = if (locationMsg.isNotBlank()) {
            "$message\n\n$locationMsg"
        } else {
            message
        }

        // ========================================
        // 1. SEND SMS FIRST
        // ========================================

        SmsSender.sendEmergencySms(
            context = context,
            contacts = contacts,
            message = emergencyMessage,
            onResult = { result ->

                try {

                    when (result) {

                        is SmsResult.Success -> {

                            NotificationHelper.sendNotification(
                                context,
                                "Emergency SMS sent successfully"
                            )

                            startEmergencyCalls(
                                context = context,
                                contacts = contacts,
                                ambulanceNumber = number
                            )
                        }

                        is SmsResult.Failed -> {

                            NotificationHelper.sendNotification(
                                context,
                                "Emergency SMS failed"
                            )
                            startEmergencyCalls(
                                context = context,
                                contacts = contacts,
                                ambulanceNumber = number
                            )
                        }
                    }

                } catch (e: Exception) {

                    com.example.lifeline.util.ErrorHandler
                        .showUnexpectedError(context)
                }
            }
        )

    } catch (e: Exception) {

        com.example.lifeline.util.ErrorHandler
            .showUnexpectedError(context)
    }
}

private fun startEmergencyCalls(
    context: Context,
    contacts: List<SavedContact>,
    ambulanceNumber: String
) {
    try {

        val emergencyManager =
            EmergencyManager(
                context = context,
                contacts = contacts,
                ambulanceNumber = ambulanceNumber,

                onDecisionRequired = { contact, onYes, onStop ->

                    EmergencyNotification.showDecision(
                        context = context,
                        contact = contact
                    )
                },

                onAllContactsFailed = { number ->

                    EmergencyNotification.showAmbulance(
                        context = context,
                        ambulanceNumber = number
                    )
                }
            )

        emergencyManager.start()

    } catch (e: Exception) {

        com.example.lifeline.util.ErrorHandler
            .showUnexpectedError(context)
    }
}

fun run(context: Context, message: String) {
    try {
        LocationHelper.getCurrentLocation(context) { location ->

            val locationText = if (location != null) {
                "${location.latitude}, ${location.longitude}"
            } else {
                ""
            }


            val contacts = ContactStorage.getContacts(context)
            val number = EmergencyNumberStorage.getNumber(context)

            if (number != null) {
                emergencyWorker(
                    context = context,
                    location = locationText,
                    contacts = contacts,
                    message = message,
                    number = number
                )
            }
        }
    } catch (e: Exception) {
        com.example.lifeline.util.ErrorHandler.showUnexpectedError(context)
    }
}

fun startEmergency(
    context: Context
) {
    try {
        val message = EmergencyMessageStorage.getMessage(context)
            ?: "🚨 EMERGENCY! I need help! 🆘 Please contact me immediately. 📍"

        run(
            context,
            message
        )
    } catch (e: Exception) {
        com.example.lifeline.util.ErrorHandler.showUnexpectedError(context)
    }
}

fun stopEmergency(
    context: Context
) {
    try {
        // 1. Cancel the call cascade
        EmergencyManager.stop()

        // 2. Notify contacts that we are safe
        val message = "🟢 Emergency cancelled. I'm safe now. ✅"

        val contacts = ContactStorage.getContacts(context)

        SmsSender.sendEmergencySms(
            context,
            contacts,
            message,
            onResult = { result ->
                when (result) {
                    is SmsResult.Success -> {
                        NotificationHelper.sendNotification(
                            context,
                            "Safe SMS sent to all contacts"
                        )
                    }
                    is SmsResult.Failed -> {
                        NotificationHelper.sendNotification(
                            context,
                            "Failed to send Safe SMS"
                        )
                    }
                }
            }
        )
    } catch (e: Exception) {
        com.example.lifeline.util.ErrorHandler.showUnexpectedError(context)
    }
}
