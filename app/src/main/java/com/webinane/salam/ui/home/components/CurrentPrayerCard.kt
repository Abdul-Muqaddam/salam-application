package com.webinane.salam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
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
fun CurrentPrayerCard(
    prayerName: String,
    time: String,
    jamaat: String,
    countdown: String,
    onSeeAll: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.sdp)
            .offset(y = (-20).sdp)
    ) {
        Card(
            shape = RoundedCornerShape(16.sdp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.sdp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.sdp)
        ) {
            Column(
                modifier = Modifier.padding(16.sdp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.sdp)
                                .background(LightBlueTeal.copy(alpha = 0.1f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = LightBlueTeal,
                                modifier = Modifier.size(16.sdp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.sdp))
                        Text(
                            text = "Next Prayer",
                            fontSize = 12.ssp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }
                    Text(
                        text = "Today",
                        fontSize = 10.ssp,
                        color = Color.Gray,
                        modifier = Modifier
                            .background(Color(0xFFF1F3F5), RoundedCornerShape(12.sdp))
                            .padding(horizontal = 8.sdp, vertical = 4.sdp)
                    )
                }

                Spacer(modifier = Modifier.height(12.sdp))

                Text(
                    text = prayerName,
                    fontSize = 28.ssp,
                    fontWeight = FontWeight.Bold,
                    color = LightBlueTeal
                )
                
                Text(
                    text = time,
                    fontSize = 22.ssp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                
                Text(
                    text = "Jamaat: $jamaat",
                    fontSize = 12.ssp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.sdp))

                // New Countdown Timer Section (HTML Match)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    LightBlueTeal.copy(alpha = 0.05f),
                                    DarkBlueNavy.copy(alpha = 0.05f)
                                )
                            ),
                            RoundedCornerShape(12.sdp)
                        )
                        .padding(12.sdp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            Icon(
                                imageVector = Icons.Default.Close, // Use a clock icon if available, for now using Star as a placeholder or search for clock
                                contentDescription = null,
                                tint = LightBlueTeal,
                                modifier = Modifier.size(14.sdp)
                            )
                            Spacer(modifier = Modifier.width(6.sdp))
                            Text(
                                text = "Time Remaining",
                                fontSize = 11.ssp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(4.sdp))
                        Text(
                            text = countdown,
                            fontSize = 26.ssp,
                            fontWeight = FontWeight.Bold,
                            color = LightBlueTeal
                        )
                        Text(
                            text = "Hours : Minutes : Seconds",
                            fontSize = 9.ssp,
                            color = Color.Gray.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.sdp))

                Button(
                    onClick = onSeeAll,
                    colors = ButtonDefaults.buttonColors(containerColor = LightBlueTeal),
                    shape = RoundedCornerShape(12.sdp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("See All Timings", fontSize = 12.ssp)
                }
            }
        }
    }
}
