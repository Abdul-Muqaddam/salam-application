package com.webinane.salam.ui.ramadan.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun RamadanTipsSection() {
    Column(modifier = Modifier.padding(horizontal = 20.sdp, vertical = 10.sdp)) {
        Text("Ramadan Tips", fontSize = 16.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
        Spacer(modifier = Modifier.height(12.sdp))
        
        TipCard("Stay Hydrated", "Drink plenty of water during Suhoor and after Iftar to avoid dehydration.", Color(0xFFF3E5F5), Color(0xFFAB47BC), R.drawable.ic_info)
        Spacer(modifier = Modifier.height(10.sdp))
        TipCard("Balanced Diet", "Include dates, fruits, vegetables, and whole grains in your meals for sustained energy.", Color(0xFFFCE4EC), Color(0xFFEC407A), R.drawable.ic_favorite)
        Spacer(modifier = Modifier.height(10.sdp))
        TipCard("Night Prayers", "Try to perform Taraweeh and Tahajjud prayers for extra spiritual rewards.", Color(0xFFE0F2F1), LightBlueTeal, R.drawable.ic_moon)
    }
}

@Composable
fun TipCard(title: String, desc: String, bg: Color, iconColor: Color, icon: Int) {
    Card(
        shape = RoundedCornerShape(16.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.sdp, Color(0xFFF1F3F5))
    ) {
        Row(modifier = Modifier.padding(12.sdp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier.size(36.sdp).background(bg, RoundedCornerShape(10.sdp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(painter = painterResource(id = icon), contentDescription = null, tint = iconColor, modifier = Modifier.size(18.sdp))
            }
            Spacer(modifier = Modifier.width(10.sdp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.ssp, color = DarkBlueNavy)
                Text(desc, fontSize = 11.ssp, color = Color.DarkGray, lineHeight = 15.ssp)
            }
        }
    }
}
