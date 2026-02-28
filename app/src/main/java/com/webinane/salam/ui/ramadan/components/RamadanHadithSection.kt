package com.webinane.salam.ui.ramadan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
fun RamadanHadithSection() {
    Column(modifier = Modifier.padding(horizontal = 20.sdp, vertical = 10.sdp)) {
        Text("Hadith of the Day", fontSize = 16.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
        Spacer(modifier = Modifier.height(12.sdp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(listOf(LightBlueTeal, com.webinane.salam.ui.theme.DarkTealGradient)),
                    shape = RoundedCornerShape(16.sdp)
                )
                .padding(16.sdp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(28.sdp).background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(7.sdp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_quote_left),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.sdp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.sdp))
                    Text("DAILY WISDOM", color = Color.White.copy(alpha = 0.9f), fontSize = 9.ssp, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(12.sdp))
                
                Text(
                    "\"When Ramadan enters, the gates of Paradise are opened, the gates of Hellfire are closed and the devils are chained.\"",
                    color = Color.White,
                    fontSize = 13.ssp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 19.ssp
                )
                
                Spacer(modifier = Modifier.height(12.sdp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(12.sdp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sahih al-Bukhari 1899", color = Color.White.copy(alpha = 0.8f), fontSize = 11.ssp)
                    Box(
                        modifier = Modifier.size(28.sdp).background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(7.sdp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.sdp)
                        )
                    }
                }
            }
        }
    }
}
