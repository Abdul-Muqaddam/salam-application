package com.webinane.salam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun Announcements() {
    Column(modifier = Modifier.padding(16.sdp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Announcements", fontSize = 14.ssp, fontWeight = FontWeight.Bold, color = LightBlueTeal)
            Text(text = "View All", fontSize = 10.ssp, color = LightBlueTeal)
        }
        Spacer(modifier = Modifier.height(10.sdp))
        Card(
            shape = RoundedCornerShape(12.sdp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(12.sdp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.sdp)
                        .background(Color(0xFFE3F2FD), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = LightBlueTeal, modifier = Modifier.size(20.sdp))
                }
                Spacer(modifier = Modifier.width(10.sdp))
                Column {
                    Text(text = "Jummah Prayer Time", fontSize = 12.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
                    Text(text = "Special sermon starts at 1:30 PM this Friday.", fontSize = 10.ssp, color = Color.Gray)
                }
            }
        }
    }
}
