package com.webinane.salam.ui.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.ui.admin.AdminScheduleUiState
import com.webinane.salam.ui.theme.AppGrayBackground
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueBackground
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun PreviewScheduleSection(state: AdminScheduleUiState) {
    Box(modifier = Modifier.padding(horizontal = 16.sdp, vertical = 8.sdp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.sdp))
                .background(Brush.linearGradient(listOf(LightBlueTeal.copy(alpha = 0.05f), LightBlueBackground)))
                .border(1.sdp, LightBlueTeal.copy(alpha = 0.2f), RoundedCornerShape(12.sdp))
                .padding(16.sdp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(id = R.drawable.ic_clock), contentDescription = null, tint = LightBlueTeal, modifier = Modifier.size(16.sdp))
                Spacer(modifier = Modifier.width(8.sdp))
                Text("Preview Schedule", fontSize = 12.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
            }
            Spacer(modifier = Modifier.height(12.sdp))
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.sdp)).background(Color.White).padding(12.sdp)
            ) {
                Column {
                    PreviewItem("Fajr", R.drawable.ic_fajr, state.fajrStart, state.fajrJamat)
                    PreviewItem("Dhuhr", R.drawable.ic_dhuhr, state.dhuhrStart, state.dhuhrJamat)
                    PreviewItem("Asr", R.drawable.ic_asr, state.asrStart, state.asrJamat)
                    PreviewItem("Maghrib", R.drawable.ic_maghrib, state.maghribStart, state.maghribJamat)
                    PreviewItem("Isha", R.drawable.ic_isha, state.ishaStart, state.ishaJamat, isLast = true)
                }
            }
        }
    }
}

@Composable
fun PreviewItem(name: String, icon: Int, start: String, jamat: String, isLast: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.sdp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = icon), contentDescription = null, tint = LightBlueTeal, modifier = Modifier.size(14.sdp))
            Spacer(modifier = Modifier.width(8.sdp))
            Text(name, fontSize = 11.ssp, fontWeight = FontWeight.SemiBold, color = DarkBlueNavy)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(start, fontSize = 11.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
            Text(" → ", fontSize = 11.ssp, color = Color.Gray)
            Text(jamat, fontSize = 11.ssp, fontWeight = FontWeight.Bold, color = LightBlueTeal)
        }
    }
    if (!isLast) HorizontalDivider(color = AppGrayBackground)
}
