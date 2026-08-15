package com.example.lifeline.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

object ErrorHandler {

    fun showUnexpectedError(context: Context) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                context,
                "Unexpected error occurred",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}