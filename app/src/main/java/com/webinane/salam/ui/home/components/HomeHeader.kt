package com.webinane.salam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun HomeHeader(currentTime: String, currentDate: String, onNavigateNotifications: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.sdp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(LightBlueTeal, DarkBlueNavy)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .padding(16.sdp)
        ) {
            // New Header Row from HTML snippet
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.sdp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.sdp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place, // Approximating mosque icon
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.sdp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.sdp))
                    Column {
                        Text(
                            text = "Salam",
                            fontSize = 15.ssp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Islamic Prayer Times",
                            fontSize = 10.ssp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
                
                Box(
                    modifier = Modifier
                        .size(38.sdp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        .clickable { onNavigateNotifications() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(20.sdp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Clock and Dates Center Aligned
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentTime.ifEmpty { "00:00" },
                    fontSize = 45.ssp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = currentDate.ifEmpty { "Loading..." },
                    fontSize = 12.ssp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = "19 Rajab 1446", // Placeholder or calculate
                    fontSize = 12.ssp,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.sdp)
                )
                Spacer(modifier = Modifier.height(20.sdp))
            }
        }
    }
}
