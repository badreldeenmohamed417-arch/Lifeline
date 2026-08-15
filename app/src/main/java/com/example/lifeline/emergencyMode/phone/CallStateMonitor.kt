package com.example.lifeline.emergencyMode.phone

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat

class CallStateMonitor(
    private val context: Context,
    private val onAnswered: () -> Unit,
    private val onEnded: () -> Unit
) {

    private val telephonyManager =
        context.getSystemService(
            Context.TELEPHONY_SERVICE
        ) as TelephonyManager

    private var lastState =
        TelephonyManager.CALL_STATE_IDLE

    private var answered = false
    private var started = false

    private val listener =
        object : PhoneStateListener() {

            override fun onCallStateChanged(
                state: Int,
                phoneNumber: String?
            ) {

                when (state) {

                    TelephonyManager.CALL_STATE_RINGING -> {
                        lastState = state
                    }

                    TelephonyManager.CALL_STATE_OFFHOOK -> {

                        /*
                         * OFFHOOK means the call became active
                         * from Android's point of view.
                         */
                        if (!answered) {
                            answered = true
                            onAnswered()
                        }

                        lastState = state
                    }

                    TelephonyManager.CALL_STATE_IDLE -> {

                        if (started) {

                            if (answered) {
                                onEnded()
                            }

                            stop()
                        }

                        lastState = state
                    }
                }
            }
        }

    fun start() {

        if (started) {
            return
        }

        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        answered = false
        lastState = TelephonyManager.CALL_STATE_IDLE
        started = true

        @Suppress("DEPRECATION")
        telephonyManager.listen(
            listener,
            PhoneStateListener.LISTEN_CALL_STATE
        )
    }

    fun stop() {

        if (!started) {
            return
        }

        @Suppress("DEPRECATION")
        telephonyManager.listen(
            listener,
            PhoneStateListener.LISTEN_NONE
        )

        started = false
    }
}