package com.example.lifeline.emergencyMode

import android.os.Bundle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lifeline.emergencyMode.phone.PhoneLauncher
import com.example.lifeline.ui.theme.InterFont
import com.example.lifeline.ui.theme.LifelineTheme

class AmbulanceActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ambulanceNumber =
            intent.getStringExtra(
                EXTRA_الإسعاف_NUMBER
            ).orEmpty()

        setContent {
            LifelineTheme {
                AmbulanceScreen(
                    ambulanceNumber = ambulanceNumber,
                    onCallAmbulance = {
                        PhoneLauncher.callNumber(
                            context = this,
                            number = ambulanceNumber
                        )
                    },
                    onStopAlert = {
                        stopEmergencyMode(this)
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        const val EXTRA_الإسعاف_NUMBER =
            "ambulance_number"
    }
}

@Composable
private fun AmbulanceScreen(
    ambulanceNumber: String,
    onCallAmbulance: () -> Unit,
    onStopAlert: () -> Unit
) {
    val background = Color(0xFF0B0F17)
    val panel = Color(0xFF141B29)
    val emergencyRed = Color(0xFFE11D2E)

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
                text = "🚨",
                fontSize = 56.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "لم يستجب أحد",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = InterFont,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "لم يرد أي من جهات اتصال الطوارئ.\nقد تحتاج إلى مساعدة طبية طارئة.",
                color = Color(0xFF9AA5B5),
                fontSize = 15.sp,
                lineHeight = 22.sp,
                fontFamily = InterFont,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Ambulance number
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = panel,
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(22.dp)
            ) {
                Column {
                    Text(
                        text = "الإسعاف",
                        color = Color(0xFF9AA5B5),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = InterFont
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (ambulanceNumber.isNotBlank())
                            ambulanceNumber
                        else
                            "لم يتم إعداد رقم",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = InterFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onCallAmbulance,
                enabled = ambulanceNumber.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = emergencyRed,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "📞  CALL الإسعاف",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = InterFont
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onStopAlert,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "إيقاف التنبيه",
                    color = Color(0xFF9AA5B5),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = InterFont
                )
            }
        }
    }
}
