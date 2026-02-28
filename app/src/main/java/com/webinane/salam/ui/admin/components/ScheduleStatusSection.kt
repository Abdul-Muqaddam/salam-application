package com.webinane.salam.ui.admin.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.ui.theme.BorderGray
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.GreenText
import com.webinane.salam.ui.theme.ScaffoldBackground
import com.webinane.salam.ui.theme.SuccessGreen
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ScheduleStatusSection(lastUpdatedTimestamp: Long? = null) {
    // Format timestamp to readable string
    val lastUpdatedText = if (lastUpdatedTimestamp != null) {
        val sdf = SimpleDateFormat("MMM dd, hh:mm a", Locale.US)
        sdf.format(Date(lastUpdatedTimestamp))
    } else {
        "Not updated yet"
    }
    
    Box(modifier = Modifier.padding(horizontal = 16.sdp)) {
        Card(
            shape = RoundedCornerShape(12.sdp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.sdp, BorderGray)
        ) {
            Column(modifier = Modifier.padding(16.sdp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.sdp).clip(CircleShape).background(SuccessGreen))
                        Spacer(modifier = Modifier.width(8.sdp))
                        Text("Schedule Status", fontSize = 11.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
                    }
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SuccessGreen.copy(alpha = 0.1f))
                            .padding(horizontal = 10.sdp, vertical = 4.sdp)
                    ) {
                        Text("Active", fontSize = 10.ssp, fontWeight = FontWeight.Medium, color = GreenText)
                    }
                }
                Spacer(modifier = Modifier.height(12.sdp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.sdp)) {
                    StatusInfoBox(label = "Last Updated", value = lastUpdatedText, modifier = Modifier.weight(1f))
                    StatusInfoBox(label = "Updated By", value = "Admin User", modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun StatusInfoBox(label: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.sdp))
            .background(ScaffoldBackground)
            .padding(12.sdp)
    ) {
        Column {
            Text(label, fontSize = 10.ssp, color = Color.Gray)
            Text(value, fontSize = 11.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
        }
    }
}
