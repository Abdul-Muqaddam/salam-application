package com.webinane.salam.ui.ramadan.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.ui.ramadan.HijriDateInfo
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlinx.coroutines.delay

import com.webinane.salam.ui.ramadan.toOrdinal

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RamadanCountdown(hijriInfo: HijriDateInfo, locationName: String) {
    var now by remember { mutableStateOf(LocalDateTime.now()) }
    
    LaunchedEffect(Unit) {
        while (true) {
            now = LocalDateTime.now()
            delay(1000)
        }
    }

    val (targetDateTime, timeRemaining) = if (hijriInfo.raw != null) {
        val currentMonth = hijriInfo.month
        val currentYear = hijriInfo.year
        
        val targetYear = if (currentMonth < 9) currentYear else currentYear + 1
        val targetRamadan = HijrahDate.of(targetYear, 9, 1)
        val targetDate = LocalDate.from(targetRamadan)
        val targetDateTime = targetDate.atStartOfDay()
        
        val totalSeconds = if (now.isBefore(targetDateTime)) {
            ChronoUnit.SECONDS.between(now, targetDateTime)
        } else {
            0L
        }
        Pair(targetDateTime, totalSeconds)
    } else {
        val fallbackDate = LocalDate.now().plusDays(15).atStartOfDay()
        Pair(fallbackDate, ChronoUnit.SECONDS.between(now, fallbackDate))
    }

    val days = timeRemaining / (24 * 3600)
    val hours = (timeRemaining % (24 * 3600)) / 3600
    val minutes = (timeRemaining % 3600) / 60
    val seconds = timeRemaining % 60

    val formattedTargetDate = targetDateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", Locale.getDefault()))

    val isRamadan = hijriInfo.isRamadan
    val currentDay = hijriInfo.day

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.sdp, vertical = 20.sdp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(if (isRamadan) Color(0xFF10B981).copy(alpha = 0.1f) else LightBlueTeal.copy(alpha = 0.05f), CircleShape)
                .padding(horizontal = 14.sdp, vertical = 5.sdp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = if (isRamadan) R.drawable.ic_check else R.drawable.ic_calendar),
                    contentDescription = null,
                    tint = if (isRamadan) Color(0xFF10B981) else LightBlueTeal,
                    modifier = Modifier.size(12.sdp)
                )
                Spacer(modifier = Modifier.width(5.sdp))
                Text(
                    text = if (isRamadan) "ACTIVE NOW" else "COUNTDOWN",
                    color = if (isRamadan) Color(0xFF10B981) else LightBlueTeal,
                    fontSize = 9.ssp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(10.sdp))
        Text(
            text = if (isRamadan) "Ramadan Mubarak!" else "Ramadan Begins In",
            fontSize = 18.ssp,
            fontWeight = FontWeight.Bold,
            color = DarkBlueNavy
        )
        Text(
            text = if (isRamadan) hijriInfo.formattedFull else formattedTargetDate,
            fontSize = 12.ssp,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(18.sdp))
        
        if (isRamadan) {
            // Show current day of Ramadan prominently
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.sdp),
                shape = RoundedCornerShape(20.sdp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = currentDay.toOrdinal(),
                            fontSize = 36.ssp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF10B981)
                        )
                        Text(
                            text = "DAY OF RAMADAN",
                            fontSize = 10.ssp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            letterSpacing = 1.ssp
                        )
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CountdownBox(days.toString().padStart(2, '0'), "Days")
                CountdownBox(hours.toString().padStart(2, '0'), "Hours")
                CountdownBox(minutes.toString().padStart(2, '0'), "Mins")
                CountdownBox(seconds.toString().padStart(2, '0'), "Secs")
            }
        }
        
        Spacer(modifier = Modifier.height(18.sdp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightBlueTeal.copy(alpha = 0.05f), RoundedCornerShape(10.sdp))
                .padding(10.sdp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_info),
                contentDescription = null,
                tint = LightBlueTeal,
                modifier = Modifier.size(14.sdp)
            )
            Spacer(modifier = Modifier.width(6.sdp))
            Text("Based on your location: $locationName", fontSize = 11.ssp, color = LightBlueTeal)
        }
    }
}

@Composable
fun CountdownBox(value: String, label: String) {
    Column(
        modifier = Modifier
            .width(64.sdp)
            .border(2.sdp, LightBlueTeal.copy(alpha = 0.2f), RoundedCornerShape(14.sdp))
            .padding(top = 14.sdp, bottom = 10.sdp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 28.ssp, fontWeight = FontWeight.Bold, color = LightBlueTeal)
        Text(label.uppercase(), fontSize = 9.ssp, color = Color.Gray, fontWeight = FontWeight.Bold)
    }
}
