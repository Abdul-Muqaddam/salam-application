package com.webinane.salam.ui.ramadan.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.webinane.salam.R
import com.webinane.salam.ui.ramadan.HijriDateInfo
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RamadanCalendarSection(
    state: com.webinane.salam.ui.ramadan.CalendarState,
    todayHijriInfo: HijriDateInfo,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.sdp, vertical = 15.sdp)) {
        Text("Islamic Calendar", fontSize = 16.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
        Spacer(modifier = Modifier.height(12.sdp))
        
        Card(
            shape = RoundedCornerShape(16.sdp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.sdp, Color(0xFFF1F3F5))
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.horizontalGradient(listOf(LightBlueTeal, com.webinane.salam.ui.theme.DarkTealGradient)))
                        .padding(12.sdp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chevron_left),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.sdp).clickable { onPreviousMonth() }
                        )
                        Text("${state.displayHijriMonth} ${state.hijriYear}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.ssp)
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chevron_right),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.sdp).clickable { onNextMonth() }
                        )
                    }
                }
                
                Column(modifier = Modifier.padding(12.sdp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        listOf("S", "M", "T", "W", "T", "F", "S").forEach {
                            Text(it, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, color = Color.Gray, fontSize = 11.ssp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.sdp))
                    
                    // Dynamic calendar grid from state
                    val chunks = state.days.chunked(7)
                    chunks.forEach { week ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            week.forEach { day ->
                                val isToday = day.isToday
                                val isCurrentMonth = day.isCurrentMonth
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.sdp)
                                        .background(if (isToday) LightBlueTeal else Color.Transparent, RoundedCornerShape(8.sdp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = day.dayOfMonth.toString(),
                                            color = if (isToday) Color.White else if (!isCurrentMonth) Color.LightGray else DarkBlueNavy,
                                            fontSize = 11.ssp,
                                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium
                                        )
                                        Text(
                                            text = day.hijriDay.toString(),
                                            color = if (isToday) Color.White.copy(alpha = 0.8f) else LightBlueTeal.copy(alpha = 0.7f),
                                            fontSize = 8.ssp
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.sdp))
                    HorizontalDivider(color = Color(0xFFF1F3F5))
                    Spacer(modifier = Modifier.height(8.sdp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.sdp).background(LightBlueTeal, CircleShape))
                            Spacer(modifier = Modifier.width(6.sdp))
                            Text("Today", fontSize = 11.ssp, color = Color.Gray)
                        }
                        
                        Text(todayHijriInfo.formattedFull, fontSize = 11.ssp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
