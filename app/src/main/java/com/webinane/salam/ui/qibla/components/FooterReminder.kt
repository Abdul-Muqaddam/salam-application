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
fun FooterReminder() {
    Card(
        shape = RoundedCornerShape(16.sdp),
        colors = CardDefaults.cardColors(
            containerColor = LightBlueTeal.copy(alpha = 0.05f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.sdp),
            horizontalArrangement = Arrangement.spacedBy(10.sdp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.sdp)
                    .background(LightBlueTeal.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_mosque),
                    contentDescription = null,
                    tint = LightBlueTeal,
                    modifier = Modifier.size(18.sdp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Prayer Reminder",
                    fontSize = 11.ssp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkBlueNavy
                )
                Spacer(modifier = Modifier.height(6.sdp))
                Text(
                    text = "Always face the Kaaba when praying Salah. May Allah accept your prayers and guide you on the straight path.",
                    fontSize = 9.ssp,
                    color = Color.Gray,
                    lineHeight = 12.ssp
                )
            }
        }
    }
}
