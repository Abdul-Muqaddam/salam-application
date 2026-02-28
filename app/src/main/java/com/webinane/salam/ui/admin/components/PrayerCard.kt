package com.webinane.salam.ui.admin.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.ui.theme.*
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun PrayerCard(
    name: String,
    subtitle: String,
    icon: Int,
    startTime: String,
    jamatTime: String,
    onStartTimeChange: (String) -> Unit,
    onJamatTimeChange: (String) -> Unit,
    showWarning: Boolean = false,
    warningText: String = "",
    showInfo: Boolean = false,
    infoText: String = "",
    enabled: Boolean = true
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.sdp),
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = if (enabled) Color.White else ScaffoldBackground),
        border = BorderStroke(1.sdp, if (enabled) BorderGray else Color.Transparent)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (enabled) LightBlueTeal.copy(alpha = 0.05f) else Color.Transparent)
                    .padding(horizontal = 16.sdp, vertical = 12.sdp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(36.sdp).clip(RoundedCornerShape(8.sdp)).background(LightBlueTeal.copy(alpha = if (enabled) 0.1f else 0.05f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painter = painterResource(id = icon), contentDescription = null, tint = if (enabled) LightBlueTeal else Color.Gray, modifier = Modifier.size(18.sdp))
                }
                Spacer(modifier = Modifier.width(10.sdp))
                Column {
                    Text(name, fontSize = 13.ssp, fontWeight = FontWeight.Bold, color = if (enabled) DarkBlueNavy else Color.Gray)
                    Text(subtitle, fontSize = 10.ssp, color = Color.Gray)
                }
            }
            
            Column(modifier = Modifier.padding(16.sdp)) {
                TimeInputField(label = "Start Time (Adhan)", value = startTime, onValueChange = onStartTimeChange, enabled = enabled)
                Spacer(modifier = Modifier.height(12.sdp))
                TimeInputField(label = "Jamat Time (Iqamah)", value = jamatTime, onValueChange = onJamatTimeChange, enabled = enabled)
                
                if (showWarning && enabled) {
                    Spacer(modifier = Modifier.height(12.sdp))
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.sdp)).background(ZakatAmber.copy(alpha = 0.1f)).border(1.sdp, ZakatAmber.copy(alpha = 0.2f), RoundedCornerShape(8.sdp)).padding(10.sdp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_info), contentDescription = null, tint = ZakatAmber, modifier = Modifier.size(14.sdp))
                        Spacer(modifier = Modifier.width(8.sdp))
                        Text(warningText, fontSize = 10.ssp, color = ZakatAmber, fontWeight = FontWeight.Medium)
                    }
                }
                
                if (showInfo && enabled) {
                    Spacer(modifier = Modifier.height(12.sdp))
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.sdp)).background(LightBlueBackground).border(1.sdp, LightBlueHighlight, RoundedCornerShape(8.sdp)).padding(10.sdp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_info), contentDescription = null, tint = LightBlueTeal, modifier = Modifier.size(14.sdp))
                        Spacer(modifier = Modifier.width(8.sdp))
                        Text(infoText, fontSize = 10.ssp, color = LightBlueTeal, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun TimeInputField(label: String, value: String, onValueChange: (String) -> Unit, enabled: Boolean = true) {
    var showDialog by remember { mutableStateOf(false) }
    
    Column {
        Text(label, fontSize = 11.ssp, fontWeight = FontWeight.Bold, color = if (enabled) DarkBlueNavy else Color.Gray, modifier = Modifier.padding(bottom = 8.sdp))
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange, // Allow manual typing
            modifier = Modifier
                .fillMaxWidth(),
            enabled = enabled,
            readOnly = false, // Allow keyboard

            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_clock), 
                    contentDescription = null, 
                    tint = if (enabled) Color.Gray else Color.Gray.copy(alpha = 0.5f), 
                    modifier = Modifier.size(16.sdp).clickable(enabled = enabled) { showDialog = true }
                )
            },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 12.ssp, 
                fontWeight = FontWeight.Bold,
                color = if (enabled) DarkBlueNavy else Color.Gray
            ),
            shape = RoundedCornerShape(12.sdp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (enabled) BorderGray else BorderGray.copy(alpha = 0.5f),
                unfocusedBorderColor = if (enabled) BorderGray else BorderGray.copy(alpha = 0.5f),
                disabledBorderColor = BorderGray.copy(alpha = 0.3f),
                focusedContainerColor = if (enabled) ScaffoldBackground else ScaffoldBackground.copy(alpha = 0.5f),
                unfocusedContainerColor = if (enabled) ScaffoldBackground else ScaffoldBackground.copy(alpha = 0.5f),
                disabledContainerColor = ScaffoldBackground.copy(alpha = 0.3f)
            )
        )
    }

    if (showDialog) {
        val currentTime = value.split(":")
        val hour = currentTime.getOrNull(0)?.toIntOrNull() ?: 5
        val minute = currentTime.getOrNull(1)?.toIntOrNull() ?: 0
        
        SimpleTimePicker(
            initialHour = hour,
            initialMinute = minute,
            onTimeSelected = { h, m -> 
                val formatted = String.format("%02d:%02d", h, m)
                onValueChange(formatted)
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTimePicker(
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = false
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onTimeSelected(timePickerState.hour, timePickerState.minute) }) {
                Text("Confirm", color = LightBlueTeal)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        },
        text = {
            TimePicker(state = timePickerState)
        },
        containerColor = Color.White
    )
}
