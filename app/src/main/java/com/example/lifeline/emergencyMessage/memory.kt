package com.example.lifeline.emergencyMessage

import android.content.Context

object EmergencyMessageStorage {

    private const val PREFS_NAME = "lifeline_emergency_message"
    private const val KEY_MESSAGE = "emergency_message"

    fun saveMessage(
        context: Context,
        message: String
    ) {
        context
            .getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putString(
                KEY_MESSAGE,
                message.trim()
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
            .getString(KEY_MESSAGE, null)
    }

    fun clearMessage(context: Context) {
        context
            .getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .remove(KEY_MESSAGE)
            .apply()
    }
}