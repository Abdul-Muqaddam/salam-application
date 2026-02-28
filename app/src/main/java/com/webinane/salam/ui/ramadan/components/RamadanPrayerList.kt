package com.webinane.salam.ui.ramadan.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.domain.model.PrayerTimes
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import com.webinane.salam.ui.theme.SuccessGreen
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun RamadanPrayerList(
    prayerTiming: PrayerTimes?,
    currentPrayer: String
) {
    Column(modifier = Modifier.padding(horizontal = 20.sdp, vertical = 10.sdp)) {
        Text("All Prayer Times", fontSize = 16.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
        Spacer(modifier = Modifier.height(12.sdp))
        
        Card(
            shape = RoundedCornerShape(16.sdp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.sdp, Color(0xFFF1F3F5)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.sdp)
        ) {
            Column {
                if (prayerTiming != null) {
                    PrayerRowItem(
                        name = "Fajr", 
                        timeDesc = "Dawn", 
                        time = prayerTiming.fajr.start, 
                        status = if (currentPrayer == "FAJR") "Current" else if (isPassed("FAJR", currentPrayer)) "Prayed" else "Upcoming", 
                        statusColor = if (currentPrayer == "FAJR") LightBlueTeal else if (isPassed("FAJR", currentPrayer)) SuccessGreen else Color.Gray,
                        isHighlighted = currentPrayer == "FAJR",
                        isFirst = true
                    )
                    PrayerRowItem(
                        name = "Dhuhr", 
                        timeDesc = "Noon", 
                        time = to24Hour(prayerTiming.dhuhr.start), 
                        status = if (currentPrayer == "DHUHR") "Current" else if (isPassed("DHUHR", currentPrayer)) "Prayed" else "Upcoming", 
                        statusColor = if (currentPrayer == "DHUHR") LightBlueTeal else if (isPassed("DHUHR", currentPrayer)) SuccessGreen else Color.Gray,
                        isHighlighted = currentPrayer == "DHUHR"
                    )
                    PrayerRowItem(
                        name = "Asr", 
                        timeDesc = "Afternoon", 
                        time = to24Hour(prayerTiming.asr.start), 
                        status = if (currentPrayer == "ASR") "Current" else if (isPassed("ASR", currentPrayer)) "Prayed" else "Upcoming", 
                        statusColor = if (currentPrayer == "ASR") LightBlueTeal else if (isPassed("ASR", currentPrayer)) SuccessGreen else Color.Gray,
                        isHighlighted = currentPrayer == "ASR"
                    )
                    PrayerRowItem(
                        name = "Maghrib", 
                        timeDesc = "Sunset", 
                        time = to24Hour(prayerTiming.maghrib.start), 
                        status = if (currentPrayer == "MAGHRIB") "Current" else if (isPassed("MAGHRIB", currentPrayer)) "Prayed" else "Upcoming", 
                        statusColor = if (currentPrayer == "MAGHRIB") LightBlueTeal else if (isPassed("MAGHRIB", currentPrayer)) SuccessGreen else Color.Gray,
                        isHighlighted = currentPrayer == "MAGHRIB"
                    )
                    PrayerRowItem(
                        name = "Isha", 
                        timeDesc = "Night", 
                        time = to24Hour(prayerTiming.isha.start), 
                        status = if (currentPrayer == "ISHA") "Current" else if (isPassed("ISHA", currentPrayer)) "Prayed" else "Upcoming", 
                        statusColor = if (currentPrayer == "ISHA") LightBlueTeal else if (isPassed("ISHA", currentPrayer)) SuccessGreen else Color.Gray,
                        isHighlighted = currentPrayer == "ISHA",
                        isLast = true
                    )
                } else {
                    Box(modifier = Modifier.fillMaxWidth().height(100.sdp), contentAlignment = Alignment.Center) {
                        Text("Loading prayer times...", color = Color.Gray, fontSize = 12.ssp)
                    }
                }
            }
        }
    }
}

private fun isPassed(prayer: String, current: String): Boolean {
    val order = listOf("FAJR", "DHUHR", "ASR", "MAGHRIB", "ISHA")
    val prayerIdx = order.indexOf(prayer)
    val currentIdx = order.indexOf(current)
    return prayerIdx < currentIdx
}

private fun to24Hour(time: String): String {
    return try {
        val parts = time.trim().split(":")
        if (parts.size == 2) {
            var hour = parts[0].toInt()
            val minute = parts[1]
            if (hour < 12) hour += 12
            "$hour:$minute"
        } else time
    } catch (e: Exception) { time }
}

@Composable
fun PrayerRowItem(
    name: String, 
    timeDesc: String, 
    time: String, 
    status: String, 
    statusColor: Color, 
    isHighlighted: Boolean = false,
    isFirst: Boolean = false,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isHighlighted) LightBlueTeal.copy(alpha = 0.05f) else Color.Transparent)
            .padding(12.sdp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.sdp)
                    .background(LightBlueTeal.copy(alpha = 0.1f), RoundedCornerShape(8.sdp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sun),
                    contentDescription = null,
                    tint = LightBlueTeal,
                    modifier = Modifier.size(18.sdp)
                )
            }
            Spacer(modifier = Modifier.width(10.sdp))
            Column {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 13.ssp, color = DarkBlueNavy)
                Text(timeDesc, fontSize = 10.ssp, color = Color.Gray)
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(time, fontWeight = FontWeight.Bold, fontSize = 16.ssp, color = DarkBlueNavy)
            Box(
                modifier = Modifier
                    .background(statusColor.copy(alpha = 0.1f), RoundedCornerShape(5.sdp))
                    .padding(horizontal = 6.sdp, vertical = 3.sdp)
            ) {
                Text(status, color = statusColor, fontSize = 9.ssp, fontWeight = FontWeight.Bold)
            }
        }
    }
    if (!isLast) HorizontalDivider(color = Color(0xFFF1F3F5), modifier = Modifier.padding(horizontal = 12.sdp))
}
