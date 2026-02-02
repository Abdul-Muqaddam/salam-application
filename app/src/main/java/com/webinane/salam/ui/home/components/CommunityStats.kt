package com.webinane.salam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import com.webinane.salam.R
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun CommunityStats() {
    Column(modifier = Modifier.padding(16.sdp)) {
        Text(
            text = stringResource(R.string.community_stats_title),
            fontSize = 14.ssp,
            fontWeight = FontWeight.Bold,
            color = LightBlueTeal,
            modifier = Modifier.padding(bottom = 12.sdp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.sdp)) {
            StatCard(modifier = Modifier.weight(1f), title = "1,247", subtitle = stringResource(R.string.members), color = LightBlueTeal)
            StatCard(modifier = Modifier.weight(1f), title = "43", subtitle = stringResource(R.string.events), color = DarkBlueNavy)
        }
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, title: String, subtitle: String, color: Color) {
    Card(
        shape = RoundedCornerShape(12.sdp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.sdp)) {
            Text(text = title, fontSize = 18.ssp, fontWeight = FontWeight.Bold, color = LightBlueTeal)
            Text(text = subtitle, fontSize = 10.ssp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.sdp))
            Box(modifier = Modifier.fillMaxWidth().height(2.sdp).background(color.copy(alpha = 0.2f))) {
                Box(modifier = Modifier.fillMaxWidth(0.6f).fillMaxHeight().background(color))
            }
        }
    }
}
