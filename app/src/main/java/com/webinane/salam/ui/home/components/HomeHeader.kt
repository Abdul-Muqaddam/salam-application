package com.webinane.salam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import com.webinane.salam.R
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
                            painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_place),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.sdp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.sdp))
                    Column {
                        Text(
                            text = "Salam", // Keep name as is or move to app_name? User didn't specify but it's likely a brand name. I'll keep it for now as it matches "مسجد سلام" in essence (brand name).
                            fontSize = 15.ssp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = stringResource(R.string.islamic_prayer_times),
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
                        painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_notifications),
                        contentDescription = stringResource(R.string.notifications_desc),
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
                    text = currentDate.ifEmpty { stringResource(R.string.loading) },
                    fontSize = 12.ssp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = stringResource(R.string.hijri_date_placeholder), // Placeholder
                    fontSize = 12.ssp,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.sdp)
                )
                Spacer(modifier = Modifier.height(20.sdp))
            }
        }
    }
}
