package com.webinane.salam.ui.qibla.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun QiblaHeader(onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(LightBlueTeal, DarkBlueNavy)
                )
            )
            .padding(top = 40.sdp, bottom = 20.sdp, start = 16.sdp, end = 16.sdp)
    ) {
        Column {
            // Top row with buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(40.sdp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.sdp)
                    )
                }
                
                Text(
                    text = "Salam",
                    color = Color.White,
                    fontSize = 12.ssp,
                    fontWeight = FontWeight.Medium
                )
                
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(40.sdp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_notifications),
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(18.sdp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.sdp))
            
            // Title
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Qibla Finder",
                    color = Color.White,
                    fontSize = 20.ssp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.sdp))
                Text(
                    text = "Find the direction of Kaaba for prayer",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.ssp
                )
            }
        }
    }
}
