package com.webinane.salam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
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
fun MosqueInfo() {
    Column(modifier = Modifier.padding(16.sdp)) {
        Text(text = "Nearby Mosque", fontSize = 14.ssp, fontWeight = FontWeight.Bold, color = LightBlueTeal)
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
                        .size(45.sdp)
                        .background(LightBlueTeal, RoundedCornerShape(10.sdp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.sdp))
                Column {
                    Text(text = "Masjid Al-Noor", fontSize = 13.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
                    Text(text = "0.8 km • New York, NY", fontSize = 10.ssp, color = Color.Gray)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(12.sdp))
                        Text(text = " 4.8 (120 reviews)", fontSize = 10.ssp, color = Color.Gray)
                    }
                }
            }
        }
    }
}
