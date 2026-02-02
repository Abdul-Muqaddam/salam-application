package com.webinane.salam.ui.qibla.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun LocationStatusSection(
    locationName: String = "Fetching...",
    latitude: Double = 0.0,
    longitude: Double = 0.0,
    currentHeading: Double = 0.0,
    locationProvider: String = "",
    onRefresh: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.sdp)
    ) {
        Column(modifier = Modifier.padding(16.sdp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.sdp)
                            .background(LightBlueTeal.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_place),
                            contentDescription = null,
                            tint = LightBlueTeal,
                            modifier = Modifier.size(20.sdp)
                        )
                    }
                    Column {
                        Text(
                            text = "Current Location",
                            fontSize = 11.ssp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkBlueNavy
                        )
                        Text(
                            text = if (latitude == 0.0) "Waiting for GPS..." else "Auto-detected",
                            fontSize = 10.ssp,
                            color = Color.Gray
                        )
                    }
                }
                
                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier
                        .size(32.sdp)
                        .background(Color(0xFFF8F9FA), RoundedCornerShape(8.sdp))
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_refresh),
                        contentDescription = "Refresh",
                        tint = LightBlueTeal,
                        modifier = Modifier.size(14.sdp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.sdp))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F9FA), RoundedCornerShape(12.sdp))
                    .padding(12.sdp)
            ) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.sdp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_place),
                            contentDescription = null,
                            tint = LightBlueTeal,
                            modifier = Modifier.size(12.sdp)
                        )
                        Text(
                            text = locationName,
                            fontSize = 12.ssp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkBlueNavy
                        )
                    }
                    Spacer(modifier = Modifier.height(4.sdp))
                    Text(
                        text = "Latitude: %.4f° N, Longitude: %.4f° E".format(latitude, longitude),
                        fontSize = 9.ssp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 18.sdp)
                    )
                    // DEBUG INFO - Remove before release
                    Text(
                        text = "Heading: %.0f° | Prov: $locationProvider", 
                        fontSize = 8.ssp,
                        color = Color.Red,
                        modifier = Modifier.padding(start = 18.sdp)
                    )
                     Text(
                        text = "Raw: %.4f, %.4f".format(latitude, longitude), 
                        fontSize = 8.ssp,
                        color = Color.Red,
                        modifier = Modifier.padding(start = 18.sdp)
                    )
                }
            }
        }
    }
}
