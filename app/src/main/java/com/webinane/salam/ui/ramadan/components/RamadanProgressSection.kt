package com.webinane.salam.ui.ramadan.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.ui.ramadan.HijriDateInfo
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RamadanProgressSection(hijriInfo: HijriDateInfo) {
    // Logic: If it's Ramadan (9th month), days completed is day-1. 
    // If it's after Ramadan, it's 30. If before, it's 0.
    val completedDays = when {
        hijriInfo.month == 9 -> (hijriInfo.day - 1).coerceIn(0, 30)
        hijriInfo.month > 9 -> 30
        else -> 0
    }
    val percentage = ((completedDays.toFloat() / 30f) * 100).toInt()

    Column(modifier = Modifier.padding(horizontal = 20.sdp, vertical = 15.sdp)) {
        Text("Ramadan Progress", fontSize = 16.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
        Spacer(modifier = Modifier.height(12.sdp))
        
        Card(
            shape = RoundedCornerShape(16.sdp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.sdp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Days Completed", color = Color.Gray, fontSize = 11.ssp)
                        Text("$completedDays / 30", fontSize = 20.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
                    }
                    // Circular Progress
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(end = 5.sdp)
                    ) {
                        CircularProgressIndicator(
                            progress = completedDays.toFloat() / 30f,
                            modifier = Modifier.size(54.sdp),
                            color = LightBlueTeal,
                            strokeWidth = 4.sdp,
                            trackColor = LightBlueTeal.copy(alpha = 0.1f)
                        )
                        Text("$percentage%", fontSize = 14.ssp, fontWeight = FontWeight.Bold, color = LightBlueTeal)
                    }
                }
                
                Spacer(modifier = Modifier.height(20.sdp))
                
                Text("Fasting Tracker • ${hijriInfo.day} ${hijriInfo.monthName}", fontSize = 13.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
                Spacer(modifier = Modifier.height(10.sdp))
                
                // 30-Day Tracker Grid
                TrackerGrid(completedDays = completedDays)
                
                Spacer(modifier = Modifier.height(20.sdp))
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.sdp)) {
                    ProgressMiniCard(modifier = Modifier.weight(1f), value = "0", label = "Fasts")
                    ProgressMiniCard(modifier = Modifier.weight(1f), value = "0", label = "Prayers")
                    ProgressMiniCard(modifier = Modifier.weight(1f), value = "0", label = "Quran")
                }
            }
        }
    }
}

@Composable
fun TrackerGrid(completedDays: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(6.sdp)) {
        for (row in 0 until 3) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.sdp)
            ) {
                for (col in 0 until 10) {
                    val day = row * 10 + col + 1
                    val isCompleted = day <= completedDays
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(4.sdp))
                            .background(
                                if (isCompleted) LightBlueTeal else LightBlueTeal.copy(alpha = 0.05f)
                            )
                            .border(
                                1.sdp,
                                if (isCompleted) LightBlueTeal else LightBlueTeal.copy(alpha = 0.1f),
                                RoundedCornerShape(4.sdp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressMiniCard(modifier: Modifier, value: String, label: String) {
    Column(
        modifier = modifier
            .background(LightBlueTeal.copy(alpha = 0.05f), RoundedCornerShape(10.sdp))
            .padding(10.sdp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 16.ssp, fontWeight = FontWeight.Bold, color = LightBlueTeal)
        Text(label, fontSize = 11.ssp, color = Color.Gray)
    }
}
