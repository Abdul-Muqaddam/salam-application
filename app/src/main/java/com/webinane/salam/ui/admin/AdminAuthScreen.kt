package com.webinane.salam.ui.admin

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.webinane.salam.R
import com.webinane.salam.data.local.PrayerSchedulePreference
import com.webinane.salam.ui.theme.*
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import org.koin.compose.koinInject


import kotlinx.coroutines.delay

@Composable
fun AdminAuthScreen(
    onNavigateBack: () -> Unit,
    onAuthenticated: () -> Unit,
    prefs: PrayerSchedulePreference = koinInject()
) {
    var inputPin by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val savedPin = remember { prefs.adminPin }

    LaunchedEffect(inputPin) {
        if (inputPin.length == 4) {
            if (inputPin == savedPin) {
                onAuthenticated()
            } else {
                showError = true
                inputPin = ""
            }
        }
    }

    LaunchedEffect(showError) {
        if (showError) {
            delay(3000)
            showError = false
        }
    }

    Scaffold(
        containerColor = Slate50,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()) // Prevent clipping on small screens
                .padding(horizontal = 24.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.sdp))

            // --- Header Section ---
            Box(
                modifier = Modifier.padding(bottom = 24.sdp),
                contentAlignment = Alignment.Center
            ) {
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val pulseScale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.2f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1500, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulseScale"
                )
                
                Box(
                    modifier = Modifier
                        .size(80.sdp)
                        .scale(pulseScale)
                        .background(LightBlueTeal.copy(alpha = 0.15f), CircleShape)
                )

                Box(
                    modifier = Modifier
                        .size(64.sdp)
                        .background(
                            brush = Brush.linearGradient(colors = listOf(LightBlueTeal, DarkBlueNavy)),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.sdp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(24.sdp)
                        .align(Alignment.BottomEnd)
                        .offset(x = (-2).sdp, y = (-2).sdp)
                        .background(Color.White, CircleShape)
                        .padding(2.sdp)
                        .background(Emerald500, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.sdp)
                    )
                }
            }

            Text(
                text = "Admin Access",
                color = Slate800,
                fontSize = 24.ssp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Enter your PIN to continue",
                color = Slate500,
                fontSize = 13.ssp,
                modifier = Modifier.padding(top = 4.sdp)
            )

            Spacer(modifier = Modifier.height(24.sdp))

            // --- Security Info Card ---
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f)),
                shape = RoundedCornerShape(12.sdp),
                border = androidx.compose.foundation.BorderStroke(1.sdp, LightBlueTeal.copy(alpha = 0.1f)),
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.sdp)
            ) {
                Row(modifier = Modifier.padding(12.sdp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(36.sdp).background(LightBlueTeal.copy(alpha = 0.1f), RoundedCornerShape(10.sdp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_info_circle),
                            contentDescription = null,
                            tint = LightBlueTeal,
                            modifier = Modifier.size(18.sdp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.sdp))
                    Column {
                        Text(text = "Protected Area", color = Slate700, fontSize = 13.ssp, fontWeight = FontWeight.SemiBold)
                        Text(text = "Only authorized admins can access settings.", color = Slate500, fontSize = 11.ssp)
                    }
                }
            }

            // PIN Display Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.sdp),
                modifier = Modifier.padding(bottom = 24.sdp)
            ) {
                repeat(4) { index ->
                    val isActive = index < inputPin.length
                    Box(
                        modifier = Modifier
                            .size(14.sdp)
                            .background(if (isActive) LightBlueTeal else Slate300, CircleShape)
                            .border(if (isActive) 0.sdp else 2.sdp, if (isActive) Color.Transparent else Slate200, CircleShape)
                    )
                }
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.sdp) // Reserve fixed space to prevent layout shift
                    .padding(bottom = 12.sdp),
                contentAlignment = Alignment.Center
            ) {
                if (showError) {
                    Text(
                        text = "Incorrect PIN. Try again.",
                        color = ErrorRed,
                        fontSize = 11.ssp
                    )
                }
            }

            // --- Keypad Section ---
            val rows = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("", "0", "back")
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.sdp)) {
                rows.forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.sdp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        row.forEach { key ->
                            if (key.isEmpty()) {
                                Spacer(modifier = Modifier.weight(1f))
                            } else {
                                KeypadButton(
                                    text = key,
                                    isBack = key == "back",
                                    onClick = {
                                        if (key == "back") {
                                            if (inputPin.isNotEmpty()) inputPin = inputPin.dropLast(1)
                                        } else if (inputPin.length < 4) {
                                            inputPin += key
                                        }
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
            
            TextButton(
                onClick = onNavigateBack,
                modifier = Modifier.padding(vertical = 12.sdp)
            ) {
                Text("Cancel", color = Slate500, fontWeight = FontWeight.Medium, fontSize = 13.ssp)
            }
            
            Spacer(modifier = Modifier.height(16.sdp))
        }
    }
}

@Composable
fun KeypadButton(
    text: String,
    isBack: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "buttonScale")

    Card(
        modifier = modifier
            .height(56.sdp)
            .scale(scale)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_backspace),
                    contentDescription = "Backspace",
                    tint = LightBlueTeal,
                    modifier = Modifier.size(22.sdp)
                )
            } else {
                Text(text = text, color = LightBlueTeal, fontSize = 20.ssp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
