package com.webinane.salam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import org.koin.androidx.compose.koinViewModel
import com.webinane.salam.ui.viewmodel.IslamicCalendarViewModel

@Composable
fun IslamicCalendar(
    viewModel: IslamicCalendarViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    
    Column(modifier = Modifier.padding(16.sdp)) {
        Text(
            text = "Islamic Calendar",
            fontSize = 14.ssp,
            fontWeight = FontWeight.Bold,
            color = LightBlueTeal,
            modifier = Modifier.padding(bottom = 12.sdp)
        )

        Card(
            shape = RoundedCornerShape(12.sdp),
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
                    IconButton(onClick = { viewModel.previousMonth() }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Previous", tint = LightBlueTeal)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "${state.currentHijriMonth} ${state.hijriYear}", fontSize = 12.ssp, fontWeight = FontWeight.Bold, color = LightBlueTeal)
                        Text(text = state.currentGregorianMonth, fontSize = 10.ssp, color = Color.Gray)
                    }
                    IconButton(onClick = { viewModel.nextMonth() }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Next", tint = LightBlueTeal)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.sdp))
                
                // Days of Week Header
                val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
                Row(modifier = Modifier.fillMaxWidth()) {
                    daysOfWeek.forEach { day ->
                        Text(
                            text = day,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center,
                            fontSize = 10.ssp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.sdp))
                
                // Calendar Grid
                val chunks = state.days.chunked(7)
                chunks.forEach { week ->
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.sdp)) {
                        week.forEach { day ->
                            val isToday = day.isToday
                            val isCurrentMonth = day.isCurrentMonth
                            
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.sdp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isToday) LightBlueTeal 
                                        else Color.Transparent
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier.height(100.sdp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = day.dayOfMonth.toString(),
                                        fontSize = 11.ssp,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.SemiBold,
                                        color = when {
                                            isToday -> Color.White
                                            isCurrentMonth -> Color.Black
                                            else -> Color.Gray.copy(alpha = 0.4f)
                                        }
                                    )
                                    Text(
                                        text = day.hijriDay.toString(),
                                        fontSize = 8.ssp,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            isToday -> Color.White.copy(alpha = 0.9f)
                                            isCurrentMonth -> LightBlueTeal
                                            else -> LightBlueTeal.copy(alpha = 0.3f)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
