package com.example.lifeline.checkNumber

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lifeline.checkNumbers.Contact
import com.example.lifeline.checkNumbers.ContactStorage
import com.example.lifeline.checkNumbers.SavedContact
import com.example.lifeline.checkNumbers.getContacts
import com.example.lifeline.permissions.LifelinePermissions
import com.example.lifeline.ui.components.ProgressSteps
import com.example.lifeline.ui.theme.InterFont

val Ink = Color(0xFF0B0F17)
val Panel = Color(0xFF141B29)
val Panel2 = Color(0xFF1B2434)
val Line = Color(0xFF283245)
val LineSoft = Color(0xFF1F2838)
val Calm = Color(0xFF33C48A)

@Composable
fun ContactsScreen(
    onContinue: () -> Unit = {},
) {
    val context = LocalContext.current

    var chosenContacts by remember {
        mutableStateOf<List<SavedContact>>(
            ContactStorage.getContacts(context)
        )
    }


    var showContactPicker by remember {
        mutableStateOf(false)
    }

    var contacts by remember {
        mutableStateOf<List<Contact>>(emptyList())
    }

    var selectedContacts by remember {
        mutableStateOf<List<Contact>>(emptyList())
    }

    val contactsLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                contacts = getContacts(context)
            }
        }

    val haveAccess =
        LifelinePermissions.hasContactsPermission(context)

    LaunchedEffect(haveAccess) {
        if (!haveAccess) {
            LifelinePermissions.requestContactsPermission(
                contactsLauncher
            )
        } else {
            contacts = getContacts(context)
        }
    }

    // ----------------------------------------
    // Screen
    // ----------------------------------------

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink)
            .padding(horizontal = 24.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            ProgressSteps(
                currentStep = 2
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Text(
                text = "Who should we alert?",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 36.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "These people receive an SMS with your location the moment you trigger Lifeline.",
                color = Color(0xFF9AA5B5),
                fontSize = 15.sp,
                lineHeight = 22.sp
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // ----------------------------------------
            // Selected contacts
            // ----------------------------------------

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = selectedContacts,
                    key = { it.id + it.phone }
                ) { contact ->

                    ContactCard(
                        name = contact.name,
                        phone = contact.phone,
                        onRemove = {

                            selectedContacts =
                                selectedContacts.filterNot {
                                    it.id == contact.id &&
                                            it.phone == contact.phone
                                }

                            chosenContacts =
                                chosenContacts.filterNot {
                                    it.id == contact.id
                                }

                            ContactStorage.saveContacts(
                                context,
                                chosenContacts
                            )
                        }
                    )
                }

                // Add Contact Button
                item {

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    AddContactButton(
                        onClick = {

                            if (
                                LifelinePermissions.hasContactsPermission(context)
                            ) {
                                showContactPicker = true
                            } else {
                                getPermissions(
                                    context as Activity,
                                    contactsLauncher
                                )
                            }
                        }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            // ----------------------------------------
            // Continue
            // ----------------------------------------

            Button(
                onClick = {
                    val savedContacts = selectedContacts.map { contact ->
                        SavedContact(
                            id = contact.id,
                            name = contact.name,
                            number = contact.phone
                        )
                    }

                    ContactStorage.saveContacts(
                        context = context,
                        contacts = savedContacts
                    )

                    onContinue()
                },
                enabled = selectedContacts.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContainerColor = Panel2,
                    disabledContentColor = Color(0xFF697386)
                )
            ) {
                Text(
                    text = "Save & Continue",
                    fontWeight = FontWeight.Bold,
                    fontFamily = InterFont
                )
            }



            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }

    // ----------------------------------------
    // Contact Picker
    // ----------------------------------------

    if (showContactPicker) {

        ContactPicker(
            contacts = contacts,
            selectedContacts = selectedContacts,
            onSelect = { contact ->

                if (
                    selectedContacts.none {
                        it.id == contact.id &&
                                it.phone == contact.phone
                    }
                ) {
                    selectedContacts =
                        selectedContacts + contact

                    chosenContacts = chosenContacts + listOf(
                        SavedContact(
                            id = contact.id,
                            name = contact.name,
                            number = contact.phone
                        )
                    )

                    ContactStorage.saveContacts(
                        context,
                        chosenContacts
                    )
                }

                showContactPicker = false
            },
            onDismiss = {
                showContactPicker = false
            }
        )
    }
}

@Composable
fun ContactCard(
    name: String,
    phone: String,
    onRemove: () -> Unit
) {
    val initials = name
        .trim()
        .split("\\s+".toRegex())
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Panel
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                colors = listOf(
                    Line,
                    LineSoft
                )
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Initials
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Panel2,
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials.ifEmpty { "?" },
                    color = Calm,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.size(14.dp)
            )

            // Name + Phone
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = phone,
                    color = Color(0xFF8E99AA),
                    fontSize = 13.sp,
                    maxLines = 1
                )
            }

            Spacer(
                modifier = Modifier.size(10.dp)
            )

            // Remove button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = Color(0xFF202A3A),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable {
                        onRemove()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "−",
                    color = Color(0xFF9AA5B5),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

fun getPermissions(
    activity: Activity,
    launcher: ManagedActivityResultLauncher<String, Boolean>
) {
    if (LifelinePermissions.hasContactsPermission(activity)) {
        return
    }

    if (LifelinePermissions.shouldOpenSettings(activity)) {
        openAppSettings(activity, "🔒 Please enable Contacts permission")
    } else {
        LifelinePermissions.requestContactsPermission(launcher)
    }
}

fun openAppSettings(activity: Activity, message: String) {
    Toast.makeText(
        activity,
        message,
        Toast.LENGTH_SHORT
    ).show()

    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:${activity.packageName}")
    )

    activity.startActivity(intent)
}
