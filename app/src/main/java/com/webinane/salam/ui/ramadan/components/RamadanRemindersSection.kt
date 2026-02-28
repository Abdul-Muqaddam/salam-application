package com.webinane.salam.ui.ramadan.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import com.webinane.salam.ui.theme.SuccessGreen
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun RamadanRemindersSection() {
    Column(modifier = Modifier.padding(horizontal = 20.sdp, vertical = 10.sdp)) {
        Text("Today's Reminders", fontSize = 16.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
        Spacer(modifier = Modifier.height(12.sdp))
        
        ReminderToggleItem("Suhoor Reminder", "Wake up at 4:30 AM", R.drawable.ic_notifications, Color(0xFFFFB800), true)
        Spacer(modifier = Modifier.height(8.sdp))
        ReminderToggleItem("Quran Reading", "Read 5 pages daily", R.drawable.ic_quran, Color(0xFF3366FF), true)
        Spacer(modifier = Modifier.height(8.sdp))
        ReminderToggleItem("Daily Charity", "Give Sadaqah today", R.drawable.ic_hand_holding_heart, SuccessGreen, false)
    }
}

@Composable
fun ReminderToggleItem(title: String, subtitle: String, icon: Int, iconColor: Color, isChecked: Boolean) {
    Card(
        shape = RoundedCornerShape(16.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.sdp, Color(0xFFF1F3F5))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(42.sdp).background(iconColor.copy(alpha = 0.1f), RoundedCornerShape(12.sdp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(id = icon), contentDescription = null, tint = iconColor, modifier = Modifier.size(18.sdp))
            }
            Spacer(modifier = Modifier.width(12.sdp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.ssp, color = DarkBlueNavy)
                Text(subtitle, fontSize = 11.ssp, color = Color.Gray)
            }
            Switch(
                checked = isChecked,
                onCheckedChange = {},
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = LightBlueTeal)
            )
        }
    }
}
