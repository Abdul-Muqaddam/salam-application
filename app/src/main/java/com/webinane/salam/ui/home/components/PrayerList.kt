package com.webinane.salam.ui.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.webinane.salam.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.domain.model.PrayerTimes
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import com.webinane.salam.ui.theme.AppGrayBackground
import com.webinane.salam.ui.theme.LightBlueHighlight
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun PrayerList(
    prayerTiming: PrayerTimes?,
    currentPrayer: String,
    onSeeAll: () -> Unit
) {
    Column(modifier = Modifier.padding(16.sdp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.prayer_times_title),
                fontSize = 14.ssp,
                fontWeight = FontWeight.Bold,
                color = LightBlueTeal
            )
            TextButton(onClick = onSeeAll) {
                Text(text = stringResource(R.string.see_full_schedule), fontSize = 10.ssp, color = LightBlueTeal)
            }
        }

        Spacer(modifier = Modifier.height(8.sdp))

        Column(verticalArrangement = Arrangement.spacedBy(10.sdp)) {
            val prayers = listOf(
                "FAJR" to (prayerTiming?.fajr?.jamaat ?: "--:--"),
                "DHUHR" to (prayerTiming?.dhuhr?.jamaat ?: "--:--"),
                "ASR" to (prayerTiming?.asr?.jamaat ?: "--:--"),
                "MAGHRIB" to (prayerTiming?.maghrib?.jamaat ?: "--:--"),
                "ISHA" to (prayerTiming?.isha?.jamaat ?: "--:--")
            )

            prayers.forEach { (name, time) ->
                PrayerItem(name = name, time = time, isHighlighted = name == currentPrayer)
            }
        }
    }
}

@Composable
fun PrayerItem(name: String, time: String, isHighlighted: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isHighlighted) 3.sdp else 1.sdp,
                shape = RoundedCornerShape(16.sdp),
                clip = false
            )
            .background(
                color = if (isHighlighted) LightBlueHighlight else Color.White,
                shape = RoundedCornerShape(16.sdp)
            )
            .border(
                width = if (isHighlighted) 1.sdp else 0.sdp,
                color = LightBlueTeal.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.sdp)
            )

    ) {
        Row(
            modifier = Modifier
                .padding(12.sdp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.sdp)
                        .background(
                            if (isHighlighted) LightBlueTeal else AppGrayBackground,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(
                            id = when (name) {
                                "FAJR" -> R.drawable.ic_fajr
                                "DHUHR" -> R.drawable.ic_dhuhr
                                "ASR" -> R.drawable.ic_asr
                                "MAGHRIB" -> R.drawable.ic_maghrib
                                "ISHA" -> R.drawable.ic_isha
                                else -> R.drawable.ic_fajr
                            }
                        ),
                        contentDescription = null,
                        tint = if (isHighlighted) Color.White else Color.Gray,
                        modifier = Modifier.size(18.sdp)
                    )
                }
                Spacer(modifier = Modifier.width(12.sdp))
                Column {
                    Text(
                        text = name,
                        fontSize = 13.ssp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlueNavy
                    )
                    Text(text = stringResource(R.string.jamaat_time), fontSize = 9.ssp, color = Color.Gray)
                }
            }
            Text(
                text = time,
                fontSize = 15.ssp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isHighlighted) LightBlueTeal else DarkBlueNavy
            )
        }
    }
}
