package com.example.lifeline.permissions

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.الموقعOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lifeline.ui.components.ProgressSteps

private val Ink = Color(0xFF0B0F17)
private val Panel = Color(0xFF141B29)
private val Panel2 = Color(0xFF1B2434)
private val Line = Color(0xFF283245)
private val LineSoft = Color(0xFF1F2838)
private val Calm = Color(0xFF33C48A)

@Composable
fun PermissionsScreen(
    onContinue: () -> Unit = {}
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val locationGranted =
            LifelinePermissions.hasLocationPermission(context)

        val smsGranted =
            LifelinePermissions.hasSmsPermission(context)

        val phoneGranted =
            LifelinePermissions.hasPhonePermission(context)

        val phoneStateGranted =
            LifelinePermissions.hasPhoneStatePermission(context)

        if (
            locationGranted &&
            smsGranted &&
            phoneGranted &&
            phoneStateGranted
        ) {
            onContinue()
        }
    }

    var permissionVersion by remember {
        mutableStateOf(0)
    }

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) {
            permissionVersion++

            val locationGranted =
                LifelinePermissions.hasLocationPermission(context)

            val smsGranted =
                LifelinePermissions.hasSmsPermission(context)

            val phoneGranted =
                LifelinePermissions.hasPhonePermission(context)

            val phoneStateGranted =
                LifelinePermissions.hasPhoneStatePermission(context)

            if (
                locationGranted &&
                smsGranted &&
                phoneGranted &&
                phoneStateGranted
            ) {
                onContinue()
            }
        }

    @Suppress("UNUSED_VARIABLE")
    val currentVersion = permissionVersion

    val locationGranted =
        LifelinePermissions.hasLocationPermission(context)

    val smsGranted =
        LifelinePermissions.hasSmsPermission(context)

    val phoneGranted =
        LifelinePermissions.hasPhonePermission(context)

    val phoneStateGranted =
        LifelinePermissions.hasPhoneStatePermission(context)

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
                currentStep = 1
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )

            Text(
                text = "بعض الأذونات لتصلك المساعدة فورًا...",
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

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    bottom = 24.dp
                )
            ) {

                item {
                    PermissionCard(
                        icon = Icons.Default.LocationOn,
                        title = "الموقع",
                        description = "يُرفق بمساعدة الطوارئ حتى يعرف أحباؤك مكانك بدقة.",
                        granted = locationGranted,
                        onClick = {
                            LifelinePermissions.requestLocationPermission(
                                launcher
                            )
                        }
                    )
                }

                item {
                    PermissionCard(
                        icon = Icons.Default.Message,
                        title = "الرسائل النصية",
                        description = "يرسل رسالة الاستغاثة إلى الأشخاص الذين تثق بهم عند تفعيل لايف لاين.",
                        granted = smsGranted,
                        onClick = {
                            LifelinePermissions.requestSmsPermission(
                                launcher
                            )
                        }
                    )
                }

                item {
                    PermissionCard(
                        icon = Icons.Default.Phone,
                        title = "المكالمات الهاتفية",
                        description = "يسمح للايف لاين ببدء مكالمة طوارئ عند تفعيله.",
                        granted = phoneGranted,
                        onClick = {
                            LifelinePermissions.requestPhonePermission(
                                launcher
                            )
                        }
                    )
                }

                item {
                    PermissionCard(
                        icon = Icons.Default.Phone,
                        title = "حالة المكالمة",
                        description = "يسمح للايف لاين بمعرفة متى يرد أحد جهات الاتصال على المكالمة أو ينهيها.",
                        granted = phoneStateGranted,
                        onClick = {
                            LifelinePermissions.requestPhoneStatePermission(
                                launcher
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PermissionCard(
    icon: ImageVector,
    title: String,
    description: String,
    granted: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = !granted,
                onClick = onClick
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (granted) Panel2 else Panel
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                colors = listOf(
                    if (granted) {
                        Calm.copy(alpha = 0.35f)
                    } else {
                        Line
                    },
                    if (granted) {
                        Calm.copy(alpha = 0.12f)
                    } else {
                        LineSoft
                    }
                )
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color = if (granted) {
                            Calm.copy(alpha = 0.12f)
                        } else {
                            Panel2
                        },
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (granted) {
                        Calm
                    } else {
                        Color(0xFF9AA5B5)
                    },
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(
                modifier = Modifier.size(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text = description,
                    color = Color(0xFF8E99AA),
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }

            Spacer(
                modifier = Modifier.size(8.dp)
            )

            Checkbox(
                checked = granted,
                onCheckedChange = null,
                colors = CheckboxDefaults.colors(
                    checkedColor = Calm,
                    uncheckedColor = Line,
                    checkmarkColor = Ink
                )
            )
        }
    }
}