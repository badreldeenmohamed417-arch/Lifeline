package com.example.lifeline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.lifeline.checkNumbers.ContactStorage
import com.example.lifeline.emergencyMessage.EmergencyMessageStorage
import com.example.lifeline.emergencyNumber.EmergencyNumberStorage
import com.example.lifeline.navigation.AppNavigation
import com.example.lifeline.permissions.LifelinePermissions
import com.example.lifeline.setup.SetupStorage
import com.example.lifeline.ui.theme.LifelineTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Lifeline)
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val startDestination = try {
            getStartDestination()
        } catch (e: Exception) {
            "permissions"
        }

        setContent {
            LifelineTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        AppNavigation(
                            startDestination = startDestination
                        )
                    }
                }
                }
            }
        }

        // Setup finished → always enter Home
        // but restore any permissions that were revoked
        if (SetupStorage.isSetupDone(this)) {
            LifelinePermissions.requestMissingPermissions(this)
        }
    }
    private fun getStartDestination(): String {
        try {
            // =========================================
            // 1. Setup already completed
            // =========================================

            if (SetupStorage.isSetupDone(this)) {
                return "home"
            }

        // =========================================
        // 2. Basic permissions
        // =========================================

        val locationGranted =
            LifelinePermissions.hasLocationPermission(this)

        val smsGranted =
            LifelinePermissions.hasSmsPermission(this)

        val phoneGranted =
            LifelinePermissions.hasPhonePermission(this)

        if (
            !locationGranted ||
            !smsGranted ||
            !phoneGranted
        ) {
            return "permissions"
        }

        // =========================================
        // 3. Contacts permission
        // =========================================

        val contactsGranted =
            LifelinePermissions.hasContactsPermission(this)

        if (!contactsGranted) {
            return "contacts"
        }

        // =========================================
        // 4. Emergency contacts
        // =========================================

        val savedContacts =
            ContactStorage.getContacts(this)

        if (savedContacts.isEmpty()) {
            return "contacts"
        }

        // =========================================
        // 5. Emergency number
        // =========================================

        val emergencyNumber =
            EmergencyNumberStorage.getNumber(this)

        if (emergencyNumber.isNullOrBlank()) {
            return "emergencyNumber"
        }

        // =========================================
        // 6. Emergency message
        // =========================================

        val emergencyMessage =
            EmergencyMessageStorage.getMessage(this)

        if (emergencyMessage.isNullOrBlank()) {
            return "emergencyMessage"
        }

        // =========================================
        // 7. Final setup step
        // =========================================

        return "actions"
    } catch (e: Exception) {
        return "permissions"
    }
}
}
