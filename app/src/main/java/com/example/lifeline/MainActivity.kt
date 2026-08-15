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
import com.example.lifeline.checkNumbers.ContactStorage
import com.example.lifeline.emergencyMessage.EmergencyMessageStorage
import com.example.lifeline.emergencyNumber.EmergencyNumberStorage
import com.example.lifeline.navigation.AppNavigation
import com.example.lifeline.permissions.LifelinePermissions
import com.example.lifeline.setup.SetupStorage
import com.example.lifeline.ui.theme.LifelineTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val startDestination = getStartDestination()

        setContent {
            LifelineTheme {

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

    private fun getStartDestination(): String {

        // 1. Basic permissions
        val locationGranted =
            LifelinePermissions.hasLocationPermission(this)

        val smsGranted =
            LifelinePermissions.hasSmsPermission(this)

        val phoneGranted =
            LifelinePermissions.hasPhonePermission(this)

        if (!locationGranted || !smsGranted || !phoneGranted) {
            return "permissions"
        }

        // 2. Contacts permission
        val contactsGranted =
            LifelinePermissions.hasContactsPermission(this)

        if (!contactsGranted) {
            return "contacts"
        }

        // 3. Emergency contacts
        val savedContacts =
            ContactStorage.getContacts(this)

        if (savedContacts.isEmpty()) {
            return "contacts"
        }

        // 4. Emergency number
        val emergencyNumber =
            EmergencyNumberStorage.getNumber(this)

        if (emergencyNumber.isNullOrBlank()) {
            return "emergencyNumber"
        }

        val emergencyMessage =
            EmergencyMessageStorage.getMessage(this)

        if (emergencyMessage.isNullOrBlank()) {
            return "emergencyMessage"
        }

        // 5. Setup completed
        if (!SetupStorage.isSetupDone(this)) {
            return "actions"
        }

        // 6. Everything is ready
        return "home"
    }
}