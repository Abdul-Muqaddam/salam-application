package com.webinane.salam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import com.webinane.salam.R
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import com.webinane.salam.ui.theme.ZakatAmber
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun QuickActions(
    onNavigateZakat: () -> Unit = {},
    onNavigateQibla: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.sdp),
        verticalArrangement = Arrangement.spacedBy(12.sdp)
    ) {
        // Row 1: Qibla and Zakat
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.sdp)
        ) {
            QuickActionButton(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.qibla_title),
                subtitle = stringResource(R.string.qibla_subtitle),
                iconRes = R.drawable.ic_place,
                color = LightBlueTeal,
                onClick = onNavigateQibla
            )
            QuickActionButton(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.zakat_title),
                subtitle = stringResource(R.string.zakat_subtitle),
                iconRes = R.drawable.ic_hand_holding_heart,
                color = LightBlueTeal,
                onClick = onNavigateZakat
            )
        }

        // Row 2: Ramadan, Quran, Duas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.sdp)
        ) {
            FeatureActionButton(
                modifier = Modifier.weight(1f),
                title = "Ramadan",
                iconRes = R.drawable.ic_ramadan,
                backgroundColor = LightBlueTeal.copy(alpha = 0.1f),
                iconColor = LightBlueTeal,
                onClick = {}
            )
            FeatureActionButton(
                modifier = Modifier.weight(1f),
                title = "Quran",
                iconRes = R.drawable.ic_quran,
                backgroundColor = LightBlueTeal.copy(alpha = 0.1f),
                iconColor = LightBlueTeal,
                onClick = {}
            )
            FeatureActionButton(
                modifier = Modifier.weight(1f),
                title = "Duas",
                iconRes = R.drawable.ic_duas,
                backgroundColor = LightBlueTeal.copy(alpha = 0.1f),
                iconColor = LightBlueTeal,
                onClick = {}
            )
        }
    }
}

@Composable
fun FeatureActionButton(
    modifier: Modifier = Modifier,
    title: String,
    iconRes: Int,
    backgroundColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(12.sdp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.sdp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.sdp)
                    .background(backgroundColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = iconRes),
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.sdp)
                )
            }
            Text(
                text = title,
                fontSize = 11.ssp,
                fontWeight = FontWeight.Medium,
                color = DarkBlueNavy
            )
        }
    }
}

@Composable
fun QuickActionButton(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    iconRes: Int,
    color: Color,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(12.sdp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = title, fontSize = 14.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
                Text(text = subtitle, fontSize = 10.ssp, color = Color.Gray)
            }
            Box(
                modifier = Modifier
                    .size(32.sdp)
                    .background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = iconRes),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.sdp)
                )
            }
        }
    }
}
