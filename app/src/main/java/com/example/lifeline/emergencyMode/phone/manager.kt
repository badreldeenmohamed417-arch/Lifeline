package com.example.lifeline.emergencyMode.phone

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.lifeline.checkNumbers.SavedContact
import com.example.lifeline.emergencyMode.phone.CallStateMonitor
import com.example.lifeline.emergencyMode.phone.PhoneLauncher

class EmergencyManager(
    private val context: Context,
    private val contacts: List<SavedContact>,
    private val ambulanceNumber: String,
    private val onDecisionRequired: (
        SavedContact,
        () -> Unit,
        () -> Unit
    ) -> Unit,
    private val onAllContactsFailed: (
        ambulanceNumber: String
    ) -> Unit
) {

    companion object {
        private const val NO_ANSWER_TIMEOUT = 30_000L
        private const val DECISION_TIMEOUT = 30_000L

        @Volatile
        private var instance: EmergencyManager? = null

        fun onUserDecision(continueNext: Boolean) {
            if (continueNext) {
                instance?.continueToNextContact()
            } else {
                instance?.stopEmergency()
            }
        }

        fun stop() {
            instance?.stopEmergency()
        }
    }

    private val handler =
        Handler(Looper.getMainLooper())

    private var currentIndex = 0
    private var emergencyActive = false
    private var waitingForDecision = false

    private var callStateMonitor:
            CallStateMonitor? = null

    private val noAnswerRunnable =
        Runnable {
            if (emergencyActive && !waitingForDecision) {
                moveToNextContact()
            }
        }

    private val decisionTimeoutRunnable =
        Runnable {
            if (emergencyActive && waitingForDecision) {
                continueToNextContact()
            }
        }

    fun start() {
        try {
            if (emergencyActive) {
                return
            }

            instance = this

            if (contacts.isEmpty()) {
                onAllContactsFailed(ambulanceNumber)
                return
            }

            emergencyActive = true
            currentIndex = 0
            waitingForDecision = false

            callCurrentContact()
        } catch (e: Exception) {
            com.example.lifeline.util.ErrorHandler.showUnexpectedError(context)
        }
    }

    private fun callCurrentContact() {

        if (!emergencyActive) {
            return
        }

        if (currentIndex >= contacts.size) {
            onAllContactsFailed(ambulanceNumber)
            return
        }

        val contact = contacts[currentIndex]

        stopCallMonitoring()

        callStateMonitor = CallStateMonitor(
            context = context,

            onAnswered = {
                onContactAnswered()
            },

            onEnded = {
                onCallEnded()
            }
        )

        callStateMonitor?.start()

        val started = PhoneLauncher.callContact(
            context = context,
            contact = contact
        )

        if (!started) {

            stopCallMonitoring()

            moveToNextContact()

            return
        }

        handler.removeCallbacks(
            noAnswerRunnable
        )

        handler.postDelayed(
            noAnswerRunnable,
            NO_ANSWER_TIMEOUT
        )
    }

    private fun onContactAnswered() {

        if (!emergencyActive) {
            return
        }

        if (waitingForDecision) {
            return
        }

        handler.removeCallbacks(
            noAnswerRunnable
        )

        waitingForDecision = true

        val contact = contacts[currentIndex]

        /*
         * Tell the UI:
         *
         * YES  -> continue
         * STOP -> finish emergency flow
         */
        onDecisionRequired(
            contact,
            {
                continueToNextContact()
            },
            {
                stopEmergency()
            }
        )

        handler.removeCallbacks(
            decisionTimeoutRunnable
        )

        handler.postDelayed(
            decisionTimeoutRunnable,
            DECISION_TIMEOUT
        )
    }

    private fun onCallEnded() {

        /*
         * If the contact answered and the user
         * hasn't decided yet, don't automatically
         * call another contact immediately.
         *
         * The 30-second decision timer controls it.
         */
    }

    fun continueToNextContact() {

        if (!emergencyActive) {
            return
        }

        waitingForDecision = false

        handler.removeCallbacks(
            decisionTimeoutRunnable
        )

        moveToNextContact()
    }

    fun stopEmergency() {

        if (!emergencyActive) {
            return
        }

        instance = null
        emergencyActive = false
        waitingForDecision = false

        handler.removeCallbacks(
            noAnswerRunnable
        )

        handler.removeCallbacks(
            decisionTimeoutRunnable
        )

        stopCallMonitoring()
    }

    private fun moveToNextContact() {

        if (!emergencyActive) {
            return
        }

        handler.removeCallbacks(
            noAnswerRunnable
        )

        handler.removeCallbacks(
            decisionTimeoutRunnable
        )

        stopCallMonitoring()

        currentIndex++

        if (currentIndex >= contacts.size) {

            onAllContactsFailed(
                ambulanceNumber
            )

            instance = null
            emergencyActive = false

            return
        }

        callCurrentContact()
    }

    private fun stopCallMonitoring() {

        callStateMonitor?.stop()
        callStateMonitor = null
    }
}