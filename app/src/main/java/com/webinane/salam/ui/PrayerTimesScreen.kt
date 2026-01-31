package com.webinane.salam.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.webinane.salam.R
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.GreenText
import com.webinane.salam.ui.theme.LightBlueBackground
import com.webinane.salam.ui.theme.LightBlueHighlight
import com.webinane.salam.ui.theme.LightBlueRow
import com.webinane.salam.ui.theme.LightBlueTeal
import com.webinane.salam.ui.theme.SectionBackground
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.webinane.salam.ui.viewmodel.PrayerTimesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PrayerTimesScreen(
    viewModel: PrayerTimesViewModel = koinViewModel(),
    onNavigateNotifications: () -> Unit
) {
    val scrollState = rememberScrollState()
    val prayerTiming by viewModel.prayerTiming.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (prayerTiming == null) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(Color.White), // Keep white for loader
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(
                    color = LightBlueTeal
                )
            }
        } else {
            // Full Screen Background
            Image(
                painter = painterResource(id = R.drawable.third_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Background Header Image
                Image(
                    painter = painterResource(id = R.drawable.top_bg_main),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.sdp),
                    contentScale = ContentScale.FillBounds
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(55.sdp))

                    // Logo
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .width(110.sdp)
                            .height(75.sdp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(6.sdp))

                    // Masjid Name
                    Text(
                        text = "مسجد سلام",
                        fontSize = 15.ssp,
                        fontWeight = FontWeight.Bold,
                        color = LightBlueTeal
                    )

                    Spacer(modifier = Modifier.height(15.sdp))

                    // Current Time
                    Text(
                        text = currentTime.ifEmpty { "00:00" },
                        fontSize = 36.ssp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlueNavy
                    )

                    // Date
                    Text(
                        text = currentDate.ifEmpty { "Loading Date..." },
                        fontSize = 14.ssp,
                        fontWeight = FontWeight.Medium,
                        color = DarkBlueNavy
                    )
                    // Hijri Date Placeholder
                    Text(
                        text = "11 Rajab 1447 AH", // Still hardcoded for now or fetch if available
                        fontSize = 14.ssp,
                        fontWeight = FontWeight.Medium,
                        color = DarkBlueNavy
                    )

                    Spacer(modifier = Modifier.height(22.sdp))

                    // Prayer Times Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.sdp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "PRAYER",
                            fontSize = 12.ssp,
                            fontWeight = FontWeight.Bold,
                            color = LightBlueTeal,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "BEGINNING",
                            fontSize = 12.ssp,
                            fontWeight = FontWeight.Bold,
                            color = LightBlueTeal,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "JAMAAT",
                            fontSize = 12.ssp,
                            fontWeight = FontWeight.Bold,
                            color = LightBlueTeal,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(6.sdp))

                    // Prayer Times List
                    val data = prayerTiming!!
                    
                    // Helper to convert 12h PM time to 24h (e.g. 1:44 -> 13:44)
                    fun to24Hour(time: String): String {
                        return try {
                            val parts = time.trim().split(":")
                            if (parts.size == 2) {
                                var hour = parts[0].toInt()
                                val minute = parts[1]
                                // If it's 1..11, assume PM and add 12. 
                                // 12 stays 12 (12:30 PM). 
                                // Note: This logic assumes these times are definitely PM if < 12.
                                if (hour < 12) {
                                    hour += 12
                                }
                                "$hour:$minute"
                            } else {
                                time
                            }
                        } catch (e: Exception) {
                            time
                        }
                    }

                    val currentPrayer by viewModel.highlightedPrayer.collectAsState()

                    PrayerTimeRow(
                        name = "FAJR", 
                        begin = data.fajrStart, 
                        jamaat = data.fajrPrayer,
                        isHighlighted = currentPrayer == "FAJR"
                    )
                    PrayerTimeRow(
                        name = "DHUHR",
                        begin = to24Hour(data.dhuhrStart),
                        jamaat = to24Hour(data.dhuhrPrayer),
                        isHighlighted = currentPrayer == "DHUHR"
                    )
                    PrayerTimeRow(
                        name = "ASR", 
                        begin = to24Hour(data.asrStart), 
                        jamaat = to24Hour(data.asrPrayer),
                        isHighlighted = currentPrayer == "ASR"
                    )
                    PrayerTimeRow(
                        name = "MAGHRIB",
                        begin = to24Hour(data.maghribStart),
                        jamaat = to24Hour(data.maghribPrayer),
                        isHighlighted = currentPrayer == "MAGHRIB"
                    )
                    PrayerTimeRow(
                        name = "ISHA", 
                        begin = to24Hour(data.ishaStart), 
                        jamaat = to24Hour(data.ishaPrayer),
                        isHighlighted = currentPrayer == "ISHA"
                    )

                    Spacer(modifier = Modifier.height(15.sdp))

                    // Bottom Info (Sunrise, Jumuah)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.sdp)
                            .background(SectionBackground, RoundedCornerShape(9.sdp))
                            .padding(12.sdp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "SUNRISE",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.ssp,
                                color = DarkBlueNavy
                            )
                            Spacer(modifier = Modifier.height(3.sdp))
                            Text(
                                text = prayerTiming?.sunrise ?: "--:--",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.ssp,
                                color = DarkBlueNavy
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "1 JUMUAH",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.ssp,
                                color = DarkBlueNavy
                            )
                            Spacer(modifier = Modifier.height(3.sdp))
                            Text(
                                text = "13:00",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.ssp,
                                color = DarkBlueNavy
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "2 JUMUAH",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.ssp,
                                color = DarkBlueNavy
                            )
                            Spacer(modifier = Modifier.height(3.sdp))
                            Text(
                                text = "14:00",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.ssp,
                                color = DarkBlueNavy
                            )
                        }
                    }
                    
                    // Add Spacer to push content up above the bottom bar
                    Spacer(modifier = Modifier.height(90.sdp))
                }

                // Bottom Navigation Bar
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .navigationBarsPadding()
                        .fillMaxWidth()
                        .height(60.sdp)
                        .clip(RoundedCornerShape(topStart = 22.sdp, topEnd = 22.sdp))
                        .background(LightBlueBackground)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                tint = LightBlueTeal,
                                modifier = Modifier.size(24.sdp)
                            )
                            Text("Home Menu", color = LightBlueTeal, fontSize = 9.ssp)
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { onNavigateNotifications() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notification",
                                tint = DarkBlueNavy,
                                modifier = Modifier.size(24.sdp)
                            )
                            Text("Notification", color = DarkBlueNavy, fontSize = 9.ssp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrayerTimeRow(name: String, begin: String, jamaat: String, isHighlighted: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.sdp, vertical = 4.sdp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Name Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isHighlighted) LightBlueHighlight else LightBlueRow
            ),
            shape = RoundedCornerShape(9.sdp),
            modifier = Modifier
                .weight(1f)
                .height(38.sdp)
                .padding(end = 3.sdp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.ssp,
                    color = if (isHighlighted) GreenText else DarkBlueNavy
                )
            }
        }

        // Begin Time Card
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .height(38.sdp)
                .padding(horizontal = 3.sdp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.button),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = begin,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.ssp,
                color = if (isHighlighted) GreenText else DarkBlueNavy
            )
        }

        // Jamaat Time Card
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .height(38.sdp)
                .padding(start = 3.sdp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.button),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = jamaat,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.ssp,
                color = if (isHighlighted) GreenText else DarkBlueNavy
            )
        }
    }
}
