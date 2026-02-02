package com.webinane.salam.ui.qibla.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ShareSection() {
    Card(
        shape = RoundedCornerShape(16.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp)
    ) {
        Column(
            modifier = Modifier.padding(16.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Share with Others",
                fontSize = 12.ssp,
                fontWeight = FontWeight.Bold,
                color = DarkBlueNavy
            )
            Spacer(modifier = Modifier.height(4.sdp))
            Text(
                text = "Help others find Qibla direction",
                fontSize = 10.ssp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(12.sdp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ShareButton(icon = R.drawable.ic_whatsapp, label = "WhatsApp")
                ShareButton(icon = R.drawable.ic_facebook, label = "Facebook")
                ShareButton(icon = R.drawable.ic_twitter, label = "Twitter")
                ShareButton(icon = R.drawable.ic_share_nodes, label = "More")
            }
        }
    }
}

@Composable
fun ShareButton(icon: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.sdp),
        modifier = Modifier
            .width(60.sdp)
            .background(Color(0xFFF8F9FA), RoundedCornerShape(12.sdp))
            .padding(vertical = 10.sdp)
    ) {
        Box(
            modifier = Modifier
                .size(40.sdp)
                .background(LightBlueTeal.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = label,
                tint = LightBlueTeal,
                modifier = Modifier.size(18.sdp)
            )
        }
        Text(
            text = label,
            fontSize = 8.ssp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
    }
}
