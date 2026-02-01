package com.webinane.salam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.webinane.salam.R
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import androidx.compose.ui.res.painterResource
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import com.webinane.salam.util.TtsManager
import org.koin.compose.koinInject

@Composable
fun DailyDuas(
    ttsManager: TtsManager = koinInject()
) {
    val duaText = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ"
    
    Column(modifier = Modifier.padding(16.sdp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Daily Duas", fontSize = 14.ssp, fontWeight = FontWeight.Bold, color = LightBlueTeal)
            Text(text = "View More", fontSize = 10.ssp, color = LightBlueTeal)
        }

        Spacer(modifier = Modifier.height(12.sdp))

        Card(
            shape = RoundedCornerShape(12.sdp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.sdp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = LightBlueTeal, modifier = Modifier.size(20.sdp))
                    Spacer(modifier = Modifier.width(8.sdp))
                    Text(text = "Morning Dua", fontSize = 12.ssp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.sdp))
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFF8F9FA), RoundedCornerShape(8.sdp)).padding(12.sdp)) {
                    Text(
                        text = duaText,
                        fontSize = 18.ssp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth(),
                        color = DarkBlueNavy
                    )
                }
                Spacer(modifier = Modifier.height(8.sdp))
                Text(text = "We have entered morning and the kingdom belongs to Allah", fontSize = 10.ssp, color = Color.Gray)
                
                Spacer(modifier = Modifier.height(12.sdp))
                
                // Listen Button
                Button(
                    onClick = { ttsManager.speak(duaText) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.sdp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightBlueTeal.copy(alpha = 0.1f),
                        contentColor = LightBlueTeal
                    ),
                    shape = RoundedCornerShape(8.sdp),
                    contentPadding = PaddingValues(0.sdp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_listen),
                            contentDescription = null,
                            modifier = Modifier.size(16.sdp)
                        )
                        Spacer(modifier = Modifier.width(8.sdp))
                        Text(
                            text = "Listen",
                            fontSize = 11.ssp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
