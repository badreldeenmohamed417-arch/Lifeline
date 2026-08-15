package com.example.lifeline.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Line = Color(0xFF283245)
private val Calm = Color(0xFF33C48A)
private val Muted = Color(0xFF667085)

@Composable
fun ProgressSteps(
    currentStep: Int,
    totalSteps: Int = 4
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            repeat(totalSteps) { index ->

                val step = index + 1

                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(
                            color = if (step <= currentStep) {
                                Calm
                            } else {
                                Line
                            },
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }

        Spacer(
            modifier = Modifier.height(7.dp)
        )

        Text(
            text = "Step $currentStep of $totalSteps",
            color = Muted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}