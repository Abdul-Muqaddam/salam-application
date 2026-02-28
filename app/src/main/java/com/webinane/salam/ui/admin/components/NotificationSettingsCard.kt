package com.webinane.salam.ui.admin.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun NotificationSettingsCard(
    adhanNotif: Boolean,
    jamatNotif: Boolean,
    reminderNotif: Boolean,
    onToggle: (String, Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.sdp, vertical = 8.sdp),
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.sdp, BorderGray)
    ) {
        Column(modifier = Modifier.padding(16.sdp)) {
            Text("Notification Settings", fontSize = 14.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
            Spacer(modifier = Modifier.height(12.sdp))
            
            NotifToggleItem(
                title = "Adhan Notifications",
                subtitle = "Alert at prayer start time",
                icon = R.drawable.ic_notifications,
                iconBg = SuccessGreen.copy(alpha = 0.1f),
                iconTint = GreenText,
                checked = adhanNotif,
                onCheckedChange = { onToggle("adhan", it) }
            )
            
            NotifToggleItem(
                title = "Jamat Notifications",
                subtitle = "Alert at congregation time",
                icon = R.drawable.ic_mosque,
                iconBg = LightBlueTeal.copy(alpha = 0.1f),
                iconTint = DarkBlueNavy,
                checked = jamatNotif,
                onCheckedChange = { onToggle("jamat", it) }
            )
        }
    }
}

@Composable
fun NotifToggleItem(
    title: String,
    subtitle: String,
    icon: Int,
    iconBg: Color,
    iconTint: Color,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(36.sdp).clip(RoundedCornerShape(8.sdp)).background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(painter = painterResource(id = icon), contentDescription = null, tint = iconTint, modifier = Modifier.size(18.sdp))
        }
        Spacer(modifier = Modifier.width(12.sdp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 12.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
            Text(subtitle, fontSize = 10.ssp, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = LightBlueTeal
            )
        )
    }
}
