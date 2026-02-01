package com.webinane.salam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun BottomNavigationBar(onNavigateNotifications: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.sdp)
            .background(Color.White)
            .padding(horizontal = 20.sdp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(icon = Icons.Default.Home, label = "Home", isSelected = true)
            BottomNavItem(icon = Icons.Default.Notifications, label = "Alerts", onClick = onNavigateNotifications)
            BottomNavItem(icon = Icons.Default.Menu, label = "More")
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) LightBlueTeal else Color.Gray,
            modifier = Modifier.size(24.sdp)
        )
        Text(
            text = label,
            fontSize = 9.ssp,
            color = if (isSelected) LightBlueTeal else Color.Gray
        )
    }
}
