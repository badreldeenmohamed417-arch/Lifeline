package com.example.lifeline.emergencyNumber

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lifeline.ui.components.ProgressSteps
import com.example.lifeline.ui.theme.InterFont

private val Ink = Color(0xFF0B0F17)
private val Panel = Color(0xFF141B29)
private val Line = Color(0xFF283245)
private val Calm = Color(0xFF33C48A)

@Composable
fun EmergencyNumberScreen(
    onContinue: () -> Unit = {}
) {
    val context = LocalContext.current
    var emergencyNumber by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ProgressSteps(
                currentStep = 3
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Text(
                text = "A few permissions, so help is instant...",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 36.sp,
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
            )

            Text(
                text = "Lifeline only asks for what it needs to reach you and reach out for you the moment you trigger it. Nothing runs in the background otherwise.",
                color = Color(0xFF9AA5B5),
                fontSize = 15.sp,
                lineHeight = 22.sp,
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            OutlinedTextField(
                value = emergencyNumber,
                onValueChange = {
                    if (it.all(Char::isDigit)) {
                        emergencyNumber = it
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Emergency Number")
                },
                placeholder = {
                    Text("e.g. 123")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,

                    focusedBorderColor = Calm,
                    unfocusedBorderColor = Line,

                    focusedLabelColor = Calm,
                    unfocusedLabelColor = Color(0xFF8E99AA),

                    cursorColor = Calm,

                    focusedContainerColor = Panel,
                    unfocusedContainerColor = Panel,

                    focusedPlaceholderColor = Color(0xFF697386),
                    unfocusedPlaceholderColor = Color(0xFF697386)
                )
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Button(
                onClick = {
                    val number = emergencyNumber.trim()

                    if (number.isEmpty()) {
                        return@Button
                    }

                    EmergencyNumberStorage.saveNumber(
                        context = context,
                        number = number
                    )

                    onContinue()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Save & Continue",
                    fontWeight = FontWeight.Bold,
                    fontFamily = InterFont
                )
            }
        }
    }
}