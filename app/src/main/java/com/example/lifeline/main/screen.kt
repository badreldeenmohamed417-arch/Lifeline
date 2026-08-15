package com.example.lifeline.main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lifeline.permissions.LifelinePermissions
import com.example.lifeline.ui.theme.InterFont
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.TextButton
import androidx.compose.ui.graphics.Brush

private val Ink = Color(0xFF0B0F17)
private val Panel = Color(0xFF141B29)
private val Calm = Color(0xFF33C48A)

@Composable
fun LifelineScreen(
    onEmergency: () -> Unit,
    onStopEmergency: () -> Unit,
    onReset: () -> Unit
) {
    val context = LocalContext.current
    var showSettingsDialog by remember { mutableStateOf(false) }

    if (showSettingsDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = {
                showSettingsDialog = false
            },
            title = {
                Text(
                    text = "Settings",
                    color = Color.White,
                    fontFamily = InterFont,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    // Reset preferences
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                showSettingsDialog = false
                                onReset()
                            }
                            .background(
                                Color(0xFF1B2434)
                            )
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Reset Preferences",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = InterFont
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Restart Lifeline setup",
                                color = Color(0xFF9AA5B5),
                                fontSize = 12.sp,
                                fontFamily = InterFont
                            )
                        }

                        Text(
                            text = "›",
                            color = Color(0xFF9AA5B5),
                            fontSize = 26.sp
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSettingsDialog = false
                    }
                ) {
                    Text(
                        text = "Done",
                        color = Color.White,
                        fontFamily = InterFont
                    )
                }
            },
            containerColor = Panel,
            shape = RoundedCornerShape(24.dp)
        )
    }

    val notificationsLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) {}

    val haveAccess =
        LifelinePermissions.hasNotificationsPermission(context)

    LaunchedEffect(haveAccess) {
        if (!haveAccess) {
            LifelinePermissions.requestNotificationPermission(notificationsLauncher)
        }
    }

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
                    onClick = { showSettingsDialog = true }
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
                modifier = Modifier.height(24.dp)
            )

            // "I'm Good Now" Button
            var stopHoldProgress by remember { mutableFloatStateOf(0f) }
            var isStopping by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Panel)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                val startTime = System.nanoTime()
                                isStopping = true
                                coroutineScope {
                                    val job = launch {
                                        while (isStopping) {
                                            val elapsed = (System.nanoTime() - startTime) / 1_000_000L
                                            stopHoldProgress = (elapsed / 2000f).coerceIn(0f, 1f)
                                            if (elapsed >= 2000L) {
                                                onStopEmergency()
                                                isStopping = false
                                                stopHoldProgress = 0f
                                                break
                                            }
                                            delay(16)
                                        }
                                    }
                                    tryAwaitRelease()
                                    isStopping = false
                                    stopHoldProgress = 0f
                                    job.cancel()
                                }
                            }
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                // Progress Background
                Box(
                    modifier = Modifier
                        .fillMaxWidth(stopHoldProgress)
                        .fillMaxHeight()
                        .align(Alignment.CenterStart)
                        .background(Calm.copy(alpha = 0.2f))
                )

                Text(
                    text = "I'm Good Now",
                    color = if (stopHoldProgress > 0) Calm else Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = InterFont
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
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
        }
    }
}

