package com.webinane.salam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Place
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
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import com.webinane.salam.ui.theme.ZakatAmber
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun QuickActions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.sdp),
        horizontalArrangement = Arrangement.spacedBy(12.sdp)
    ) {
        QuickActionButton(
            modifier = Modifier.weight(1f),
            title = "Qibla",
            subtitle = "Compass",
            icon = Icons.Default.Place,
            color = LightBlueTeal,
            onClick = {}
        )
        QuickActionButton(
            modifier = Modifier.weight(1f),
            title = "Zakat",
            subtitle = "Calculator",
            icon = Icons.Default.Favorite,
            color = ZakatAmber,
            onClick = {}
        )
    }
}

@Composable
fun QuickActionButton(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
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
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.sdp))
            }
        }
    }
}
