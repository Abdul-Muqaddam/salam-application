package com.webinane.salam.ui.dua.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.webinane.salam.domain.model.dua.DuaCategory
import com.webinane.salam.ui.theme.*
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun DuaCategoryCard(
    category: DuaCategory,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(2.sdp), // Small padding to prevent shadow clipping
        shape = RoundedCornerShape(16.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.sdp)
                    .background(LightBlueTeal.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = category.iconRes),
                    contentDescription = null,
                    tint = LightBlueTeal,
                    modifier = Modifier.size(20.sdp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.sdp))
            
            Text(
                text = category.name,
                fontSize = 12.ssp,
                fontWeight = FontWeight.Bold,
                color = DarkBlueNavy,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "${category.duaCount} Duas",
                fontSize = 10.ssp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}
