package com.example.lifeline.emergencyNumber

import android.content.Context

object EmergencyNumberStorage {

    private const val PREFS_NAME = "lifeline_emergency"
    private const val KEY_NUMBER = "emergency_number"

    fun saveNumber(
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

    fun getNumber(
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