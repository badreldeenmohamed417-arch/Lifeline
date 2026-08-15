package com.example.lifeline.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context

object WidgetInstaller {

    fun requestWidget(context: Context): Boolean {

        val appWidgetManager =
            AppWidgetManager.getInstance(context)

        if (!appWidgetManager.isRequestPinAppWidgetSupported) {
            return false
        }

        val provider = ComponentName(
            context,
            LifelineWidget::class.java
        )

        return appWidgetManager.requestPinAppWidget(
            provider,
            null,
            null
        )
    }
}