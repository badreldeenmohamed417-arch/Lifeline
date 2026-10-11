package com.example.lifeline.emergencyMode

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lifeline.emergencyMode.phone.EmergencyManager
import com.example.lifeline.ui.theme.InterFont
import com.example.lifeline.ui.theme.LifelineTheme

class EmergencyDecisionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LifelineTheme {
                EmergencyDecisionScreen(
                    onYes = {
                        EmergencyManager.onUserDecision(continueNext = true)
                        finish()
                    },
                    onStop = {
                        EmergencyManager.onUserDecision(continueNext = false)
                        finish()
                    },
                    onTimeout = {
                        EmergencyManager.onUserDecision(continueNext = true)
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
private fun EmergencyDecisionScreen(
    onYes: () -> Unit,
    onStop: () -> Unit,
    onTimeout: () -> Unit
) {
    val background = Color(0xFF0B0F17)
    val calm = Color(0xFF33C48A)

    var millisLeft by remember { mutableLongStateOf(30_000L) }

    DisposableEffect(Unit) {
        val timer = object : CountDownTimer(millisLeft, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                millisLeft = millisUntilFinished
            }

            override fun onFinish() {
                onTimeout()
            }
        }.start()

        onDispose {
            timer.cancel()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "📞",
                fontSize = 56.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "تم الرد على المكالمة",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = InterFont,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "هل تريد من لايف لاين إخطار\nجهات اتصال الطوارئ الأخرى؟",
                color = Color(0xFF9AA5B5),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontFamily = InterFont,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Continuing automatically in ${millisLeft / 1000}s",
                color = calm,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = InterFont
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onYes,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "نعم، أبلغ الآخرين",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = InterFont
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onStop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "إيقاف التنبيه",
                    color = Color(0xFFFF5757),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = InterFont
                )
            }
        }
    }
}
