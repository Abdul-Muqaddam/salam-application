package com.webinane.salam.ui.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.ui.theme.AppGrayBackground
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun AdminPageHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.sdp, vertical = 20.sdp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.sdp)
                    .clip(RoundedCornerShape(12.sdp))
                    .background(LightBlueTeal.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_clock), contentDescription = null, tint = LightBlueTeal, modifier = Modifier.size(24.sdp))
            }
            Spacer(modifier = Modifier.width(12.sdp))
            Column {
                Text("Fixed Prayer Schedule", fontSize = 18.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
                Text("Admin Configuration", fontSize = 11.ssp, color = Color.Gray, fontWeight = FontWeight.Medium)
            }
        }
        Spacer(modifier = Modifier.height(12.sdp))
        Text(
            "These prayer times will repeat daily and notifications will be sent automatically until updated.",
            fontSize = 12.ssp,
            color = Color.Gray,
            lineHeight = 18.ssp
        )
    }
    HorizontalDivider(color = AppGrayBackground)
}
