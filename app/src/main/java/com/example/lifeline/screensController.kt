package com.example.lifeline.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lifeline.actions.QuickActionsScreen
import com.example.lifeline.checkNumber.ContactsScreen
import com.example.lifeline.emergencyMessage.EmergencyMessageScreen
import com.example.lifeline.emergencyMode.startEmergencyMode
import com.example.lifeline.emergencyNumber.EmergencyNumberScreen
import com.example.lifeline.main.LifelineScreen
import com.example.lifeline.permissions.PermissionsScreen

@Composable
fun AppNavigation(
    startDestination: String
) {
    val context = LocalContext.current
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // ─────────────────────────────
        // 1. Permissions
        // ─────────────────────────────

        composable("permissions") {
            PermissionsScreen(
                onContinue = {
                    navController.navigate("contacts")
                }
            )
        }

        // ─────────────────────────────
        // 2. Contacts
        // ─────────────────────────────

        composable("contacts") {
            ContactsScreen(
                onContinue = {
                    navController.navigate("emergencyNumber")
                },
            )
        }

        // ─────────────────────────────
        // 3. Action
        // ─────────────────────────────

        composable("action") {
            QuickActionsScreen(
                onContinue = {
                    navController.navigate("home") {
                        popUpTo("action") {
                            inclusive = true
                        }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // ─────────────────────────────
        // 3. EmergencyNumber
        // ─────────────────────────────

        composable("emergencyNumber") {
            EmergencyNumberScreen(
                onContinue = {
                    navController.navigate("emergencyMessage")
                }
            )
        }

        composable("emergencyMessage") {
            EmergencyMessageScreen(
                onContinue = {
                    navController.navigate("action")
                }
            )
        }

        // ─────────────────────────────
        // 4. Lifeline
        // ─────────────────────────────

        composable("home") {
            LifelineScreen(
                onEmergency = {
                    startEmergencyMode(
                        context
                    )
                },
                onStopEmergency = {
                    com.example.lifeline.emergencyMode.stopEmergencyMode(
                        context
                    )
                },
                onReset = {
                    // 1. Clear Storage
                    com.example.lifeline.checkNumbers.ContactStorage.clearContacts(context)
                    com.example.lifeline.emergencyNumber.EmergencyNumberStorage.clearNumber(context)
                    com.example.lifeline.emergencyMessage.EmergencyMessageStorage.clearMessage(context)
                    com.example.lifeline.setup.SetupStorage.resetSetup(context)

                    // 2. Navigate back to start
                    navController.navigate("contacts") {
                        popUpTo("home") {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}