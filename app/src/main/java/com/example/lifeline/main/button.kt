package com.example.lifeline.main

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lifeline.ui.theme.InterFont
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    val infiniteTransition = rememberInfiniteTransition(
        label = "emergency_ripples"
    )

    val rippleProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.18f,
        targetValue = 0.32f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = EaseInOut
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Box(
        modifier = Modifier.size(290.dp),
        contentAlignment = Alignment.Center
    ) {

        // ----------------------------------------
        // Glowing waves
        // ----------------------------------------

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val center = this.center
            val maxRadius = size.minDimension / 2f

            // Wave 1
            val radius1 = maxRadius * rippleProgress

            drawCircle(
                color = Color(0xFFFF3B3B).copy(
                    alpha = (1f - rippleProgress) * 0.18f
                ),
                radius = radius1,
                center = center,
                style = Stroke(
                    width = 2.dp.toPx()
                )
            )

            // Wave 2
            val secondProgress =
                (rippleProgress + 0.5f) % 1f

            val radius2 = maxRadius * secondProgress

            drawCircle(
                color = Color(0xFFFF5555).copy(
                    alpha = (1f - secondProgress) * 0.14f
                ),
                radius = radius2,
                center = center,
                style = Stroke(
                    width = 2.dp.toPx()
                )
            )

            // Soft outer glow
            drawCircle(
                color = Color(0xFFFF2222).copy(
                    alpha = glowAlpha
                ),
                radius = maxRadius * 0.78f
            )
        }

        // ----------------------------------------
        // Emergency Button
        // ----------------------------------------

        val infiniteTransition = rememberInfiniteTransition(
            label = "emergency_effects"
        )

        val ripple1 by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1800,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "ripple1"
        )

        val ripple2 by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1800,
                    delayMillis = 600,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "ripple2"
        )

        val ripple3 by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1800,
                    delayMillis = 1200,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "ripple3"
        )

        val glowPulse by infiniteTransition.animateFloat(
            initialValue = 0.45f,
            targetValue = 0.8f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 900,
                    easing = EaseInOut
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "glowPulse"
        )

        Box(
            modifier = Modifier.size(340.dp),
            contentAlignment = Alignment.Center
        ) {

            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {

                val center = this.center

                // نصف قطر الزر نفسه
                val buttonRadius = 110.dp.toPx()

                // أقصى مسافة تنتشر إليها الموجة
                val maxRippleRadius = 165.dp.toPx()

                // ----------------------------------------
                // Strong Glow
                // ----------------------------------------

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF1F1F).copy(
                                alpha = glowPulse
                            ),
                            Color(0xFFFF1F1F).copy(
                                alpha = glowPulse * 0.45f
                            ),
                            Color(0xFFFF1F1F).copy(
                                alpha = 0.0f
                            )
                        ),
                        center = center,
                        radius = 155.dp.toPx()
                    ),
                    radius = 155.dp.toPx(),
                    center = center
                )

                // ----------------------------------------
                // Ripple function
                // ----------------------------------------

                fun drawRipple(
                    progress: Float,
                    maxAlpha: Float
                ) {
                    val radius =
                        buttonRadius +
                                (maxRippleRadius - buttonRadius) * progress

                    val alpha =
                        (1f - progress) * maxAlpha

                    drawCircle(
                        color = Color(0xFFFF3B3B).copy(
                            alpha = alpha
                        ),
                        radius = radius,
                        center = center,
                        style = Stroke(
                            width = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }

                // ----------------------------------------
                // Waves spreading OUTWARD
                // ----------------------------------------

                drawRipple(
                    progress = ripple1,
                    maxAlpha = 0.65f
                )

                drawRipple(
                    progress = ripple2,
                    maxAlpha = 0.5f
                )

                drawRipple(
                    progress = ripple3,
                    maxAlpha = 0.4f
                )
            }

            // ----------------------------------------
            // Emergency Button
            // ----------------------------------------

            Box(
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFF5757),
                                Color(0xFFE11D2E),
                                Color(0xFF991B1B)
                            )
                        )
                    )
                    .pointerInput(Unit) {

                        detectTapGestures(
                            onPress = {

                                isHolding = true
                                triggered = false
                                progress = 0f

                                coroutineScope {

                                    val startTime =
                                        System.nanoTime()

                                    val timerJob = launch {

                                        while (true) {

                                            val elapsed =
                                                (
                                                        System.nanoTime() -
                                                                startTime
                                                        ) / 1_000_000L

                                            progress =
                                                (elapsed / 2000f)
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

                                    val released =
                                        tryAwaitRelease()

                                    if (
                                        released &&
                                        !triggered
                                    ) {
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

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "HELP",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = InterFont,
                        letterSpacing = 2.sp
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = if (isHolding) {
                            "${(progress * 100).toInt()}%"
                        } else {
                            "Hold for 2 seconds"
                        },
                        color = Color.White.copy(
                            alpha = 0.75f
                        ),
                        fontSize = 12.sp,
                        fontFamily = InterFont
                    )
                }
            }
        }
    }
}