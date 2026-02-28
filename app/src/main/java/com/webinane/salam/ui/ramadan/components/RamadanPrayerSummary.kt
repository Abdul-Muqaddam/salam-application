package com.webinane.salam.ui.ramadan.components

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.webinane.salam.domain.model.PrayerTimes
import com.webinane.salam.ui.ramadan.HijriDateInfo
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RamadanPrayerSummary(
    hijriInfo: HijriDateInfo,
    prayerTiming: PrayerTimes?,
    countdown: String
) {
    val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.getDefault()))
    
    Column(modifier = Modifier.padding(horizontal = 20.sdp, vertical = 10.sdp)) {
        Text("Today's Prayer Times", fontSize = 16.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
        Column() {
            Text(currentDate, fontSize = 11.ssp, color = Color.Gray)
            Text(hijriInfo.formattedFull, fontSize = 11.ssp, color = LightBlueTeal, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(12.sdp))
        
        if (prayerTiming != null) {
            PrayerGradientCard(
                title = "Iftar Time",
                subtitle = "Break Your Fast",
                time = to24Hour(prayerTiming.maghrib.start),
                period = "PM",
                info = "Time until Iftar",
                infoValue = countdown
            )
            
            Spacer(modifier = Modifier.height(10.sdp))
            
            PrayerGradientCard(
                title = "Suhoor Starts",
                subtitle = "Begin Predawn Meal",
                time = subtractHour(prayerTiming.fajr.start),
                period = "AM",
                info = "Recommended Awakening",
                infoValue = "1 Hour Early"
            )
            
            Spacer(modifier = Modifier.height(10.sdp))

            PrayerGradientCard(
                title = "Suhoor Ends",
                subtitle = "Stop Eating & Drinking",
                time = prayerTiming.fajr.start,
                period = "AM",
                info = "Today's Suhoor Ends",
                infoValue = "Fajr Start"
            )
        } else {
            Box(modifier = Modifier.fillMaxWidth().height(100.sdp), contentAlignment = Alignment.Center) {
                Text("Loading prayer times...", color = Color.Gray, fontSize = 12.ssp)
            }
        }
    }
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

private fun subtractHour(timeStr: String): String {
    return try {
        val parts = timeStr.split(":")
        var hour = parts[0].toInt()
        val minute = parts[1]
        hour = (hour - 1 + 24) % 24
        String.format(Locale.US, "%02d:%s", hour, minute)
    } catch (e: Exception) {
        timeStr
    }
}

@Composable
fun PrayerGradientCard(title: String, subtitle: String, time: String, period: String, info: String, infoValue: String) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.sdp)
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.sdp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_moon),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.sdp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.sdp))
                    Column {
                        Text(title, color = Color.White, fontSize = 14.ssp, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(subtitle, color = Color.White.copy(alpha = 0.8f), fontSize = 11.ssp, maxLines = 1)
                    }
                }
                Spacer(modifier = Modifier.width(8.sdp))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = time, 
                        color = Color.White, 
                        fontSize = 22.ssp, 
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        softWrap = false
                    )
                    Text(period, color = Color.White.copy(alpha = 0.8f), fontSize = 11.ssp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(12.sdp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.sdp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(info, color = Color.White.copy(alpha = 0.8f), fontSize = 11.ssp)
                Text(infoValue, color = Color.White, fontSize = 12.ssp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
