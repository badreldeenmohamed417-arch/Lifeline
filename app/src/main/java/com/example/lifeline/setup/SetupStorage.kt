package com.example.lifeline.setup

import android.content.Context

object SetupStorage {

    private const val PREFS_NAME = "lifeline_setup"
    private const val KEY_SETUP_DONE = "setup_done"

    fun isSetupDone(context: Context): Boolean {
        return context
            .getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            .getBoolean(KEY_SETUP_DONE, false)
    }

    fun setSetupDone(
        context: Context,
        done: Boolean = true
    ) {
        context
            .getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putBoolean(
                KEY_SETUP_DONE,
                done
            )
            .apply()
    }

    fun resetSetup(context: Context) {
        context
            .getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .remove(KEY_SETUP_DONE)
            .apply()
    }
}