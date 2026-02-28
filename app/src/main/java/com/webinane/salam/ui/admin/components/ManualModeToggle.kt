package com.webinane.salam.ui.admin.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.ui.theme.*
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ManualModeToggle(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = if (isEnabled) LightBlueBackground else Color.White),
        border = BorderStroke(1.sdp, if (isEnabled) LightBlueTeal.copy(alpha = 0.5f) else BorderGray)
    ) {
        Row(
            modifier = Modifier.padding(16.sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isEnabled) "Manual Schedule Enabled" else "Automatic (GPS) Mode",
                    fontSize = 12.ssp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlueNavy
                )
                Text(
                    text = if (isEnabled) "Using fixed times set below" else "Using location-based calculations",
                    fontSize = 10.ssp,
                    color = Color.Gray
                )
            }
            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = LightBlueTeal
                )
            )
        }
    }
}
