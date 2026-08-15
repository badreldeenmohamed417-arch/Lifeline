package com.example.lifeline.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lifeline.ui.theme.InterFont
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val Ink = Color(0xFF0B0F17)
private val Panel = Color(0xFF141B29)
private val Calm = Color(0xFF33C48A)

@Composable
fun MainScreen(
    onEmergency: () -> Unit,
    onSettings: () -> Unit,
    onContacts: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink)
            .padding(24.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lifeline",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = InterFont
                )

                IconButton(
                    onClick = onSettings
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            EmergencyButton(
                onEmergency = onEmergency
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Hold for 2 seconds",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color(0xFF9AA5B5),
                fontSize = 14.sp,
                fontFamily = InterFont
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            Calm,
                            CircleShape
                        )
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "System is ready",
                    color = Color(0xFF9AA5B5),
                    fontSize = 13.sp
                )
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onContacts),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Panel
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Emergency Contacts",
                        modifier = Modifier.weight(1f),
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "›",
                        color = Color(0xFF9AA5B5),
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
fun EmergencyButton(
    onEmergency: () -> Unit
) {
    var isHolding by remember {
        mutableStateOf(false)
    }

    var progress by remember {
        mutableFloatStateOf(0f)
    }

    var triggered by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .size(220.dp)
            .clip(CircleShape)
            .background(Color(0xFFB91C1C))
            .pointerInput(Unit) {

                detectTapGestures(
                    onPress = {

                        isHolding = true
                        triggered = false
                        progress = 0f

                        coroutineScope {

                            val startTime = System.nanoTime()

                            val timerJob = launch {
                                while (true) {

                                    val elapsed =
                                        (System.nanoTime() - startTime) / 1_000_000L

                                    progress = (elapsed / 2000f)
                                        .coerceIn(0f, 1f)

                                    if (elapsed >= 2000L) {
                                        progress = 1f
                                        triggered = true
                                        onEmergency()
                                        break
                                    }

                                    delay(16L)
                                }
                            }

                            val released = tryAwaitRelease()

                            if (released && !triggered) {
                                timerJob.cancel()

                                isHolding = false
                                progress = 0f
                            } else {
                                timerJob.cancel()

                                isHolding = false
                            }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawArc(
                color = Color.White,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(
                    width = 8.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "🚨",
                fontSize = 42.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "LIFELINE",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = InterFont
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = if (isHolding) {
                    "${(progress * 100).toInt()}%"
                } else {
                    "Hold for 2 seconds"
                },
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 12.sp,
                fontFamily = InterFont
            )
        }
    }
}