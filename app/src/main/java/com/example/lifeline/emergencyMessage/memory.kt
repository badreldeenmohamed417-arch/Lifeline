package com.example.lifeline.emergencyMessage

import android.content.Context

object EmergencyMessageStorage {

    private const val PREFS_NAME = "lifeline_emergency"
    private const val KEY_NUMBER = "emergency_number"

    fun saveMessage(
        context: Context,
        number: String
    ) {
        context
            .getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putString(
                KEY_NUMBER,
                number.trim()
            )
            .apply()
    }

    fun getMessage(
        context: Context
    ): String? {
        return context
            .getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            .getString(KEY_NUMBER, null)
    }
}