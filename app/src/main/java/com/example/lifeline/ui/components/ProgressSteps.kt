package com.example.lifeline.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Panel = Color(0xFF141B29)
private val Line = Color(0xFF283245)
private val Calm = Color(0xFF33C48A)
private val Muted = Color(0xFF667085)

@Composable
fun ProgressSteps(
    currentStep: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Step(
            title = "Permissions",
            step = 1,
            currentStep = currentStep
        )

        Spacer(modifier = Modifier.width(8.dp))

        Connector(
            active = currentStep >= 2
        )

        Spacer(modifier = Modifier.width(8.dp))

        Step(
            title = "Contacts",
            step = 2,
            currentStep = currentStep
        )

        Spacer(modifier = Modifier.width(8.dp))

        Connector(
            active = currentStep >= 3
        )

        Spacer(modifier = Modifier.width(8.dp))

        Step(
            title = "Action",
            step = 3,
            currentStep = currentStep
        )
    }
}

@Composable
private fun Step(
    title: String,
    step: Int,
    currentStep: Int
) {
    val isCompleted = step < currentStep
    val isCurrent = step == currentStep

    val background = when {
        isCompleted -> Calm
        isCurrent -> Panel
        else -> Color(0xFF0F141E)
    }

    val textColor = when {
        isCompleted -> Color(0xFF0B0F17)
        isCurrent -> Color.White
        else -> Muted
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .width(8.dp)
                .height(8.dp)
                .background(
                    color = if (isCurrent || isCompleted) Calm else Line,
                    shape = RoundedCornerShape(50)
                )
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = title,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = if (isCurrent) {
                FontWeight.Bold
            } else {
                FontWeight.Medium
            }
        )
    }
}

@Composable
private fun Connector(
    active: Boolean
) {
    Spacer(
        modifier = Modifier
            .width(18.dp)
            .height(1.dp)
            .background(
                if (active) Calm else Line
            )
    )
}