package com.example.lifeline.checkNumber

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lifeline.checkNumbers.Contact
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.Alignment

@Composable
fun ContactPicker(
    contacts: List<Contact>,
    selectedContacts: List<Contact>,
    onSelect: (Contact) -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = Ink
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                var searchQuery by remember {
                    mutableStateOf("")
                }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Search contacts...",
                            color = Color(0xFF697386)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Calm,
                        unfocusedBorderColor = Line,
                        cursorColor = Calm,
                        focusedContainerColor = Panel,
                        unfocusedContainerColor = Panel
                    )
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                val filteredContacts = remember(contacts, searchQuery) {
                    if (searchQuery.isBlank()) {
                        contacts
                    } else {
                        contacts.filter { contact ->
                            contact.name.contains(
                                searchQuery.trim(),
                                ignoreCase = true
                            ) || contact.phone.contains(
                                searchQuery.trim(),
                                ignoreCase = true
                            )
                        }
                    }
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(420.dp)
                ) {

                    items(
                        items = filteredContacts,
                        key = { it.id + it.phone }
                    ) { contact ->

                        val alreadySelected =
                            selectedContacts.any {
                                it.id == contact.id &&
                                        it.phone == contact.phone
                            }

                        ContactPickerRow(
                            contact = contact,
                            selected = alreadySelected,
                            onClick = {
                                if (!alreadySelected) {
                                    onSelect(contact)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContactPickerRow(
    contact: Contact,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = !selected,
                onClick = onClick
            )
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    color = Panel2,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = contact.name
                    .trim()
                    .split(" ")
                    .filter { it.isNotEmpty() }
                    .take(2)
                    .joinToString("") {
                        it.first().uppercase()
                    },
                color = Calm,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.size(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = contact.name,
                color = if (selected)
                    Color(0xFF697386)
                else
                    Color.White,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = contact.phone,
                color = Color(0xFF8E99AA),
                fontSize = 13.sp
            )
        }

        if (selected) {
            Text(
                text = "Added",
                color = Calm,
                fontSize = 12.sp
            )
        }
    }
}