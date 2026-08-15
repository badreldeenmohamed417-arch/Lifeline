package com.example.lifeline.actions

import android.content.Context

object ActionPreferences {

    private const val PREFS_NAME = "lifeline_preferences"

    private const val KEY_WIDGET_ENABLED = "widget_enabled"
    private const val KEY_VOLUME_DOWN_ENABLED = "volume_down_enabled"

    fun saveActions(
        context: Context,
        widgetEnabled: Boolean,
        volumeDownEnabled: Boolean
    ) {
        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ).edit()
            .putBoolean(KEY_WIDGET_ENABLED, widgetEnabled)
            .putBoolean(KEY_VOLUME_DOWN_ENABLED, volumeDownEnabled)
            .apply()
    }

    fun isWidgetEnabled(context: Context): Boolean {
        return context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ).getBoolean(KEY_WIDGET_ENABLED, false)
    }

    fun isVolumeDownEnabled(context: Context): Boolean {
        return context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ).getBoolean(KEY_VOLUME_DOWN_ENABLED, false)
    }
}