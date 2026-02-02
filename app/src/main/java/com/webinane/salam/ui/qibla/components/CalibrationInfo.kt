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
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun CalibrationInfo() {
    Card(
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E6)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.sdp),
            horizontalArrangement = Arrangement.spacedBy(10.sdp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.sdp)
                    .background(Color(0xFFFFC107).copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_info),
                    contentDescription = null,
                    tint = Color(0xFFFFA000),
                    modifier = Modifier.size(18.sdp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Calibration Required",
                    fontSize = 11.ssp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF6D4C00)
                )
                Spacer(modifier = Modifier.height(4.sdp))
                Text(
                    text = "Move your device in a figure-8 pattern to calibrate the compass for accurate Qibla direction.",
                    fontSize = 9.ssp,
                    color = Color(0xFF8B6914),
                    lineHeight = 12.ssp
                )
            }
        }
    }
}
