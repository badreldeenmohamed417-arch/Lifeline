package com.example.lifeline.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lifeline.checkNumber.ContactsScreen
import com.example.lifeline.permissions.PermissionsScreen
import com.example.lifeline.actions.QuickActionsScreen
import com.example.lifeline.emergencyMessage.EmergencyMessageScreen
import com.example.lifeline.emergencyNumber.EmergencyNumberScreen

@Composable
fun AppNavigation(
    startDestination: String
) {
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
                    navController.navigate("emergency_number")
                },
            )
        }

        // ─────────────────────────────
        // 3. Action
        // ─────────────────────────────

        composable("action") {
            QuickActionsScreen(
                onContinue = {
                    navController.navigate("lifeline")
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

//        composable("lifeline") {
//            LifelineScreen()
//        }
    }
}