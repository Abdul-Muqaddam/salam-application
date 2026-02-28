package com.webinane.salam.ui.ramadan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
fun RamadanCommunitySection() {
    Column(modifier = Modifier.padding(horizontal = 20.sdp, vertical = 15.sdp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Community", fontSize = 16.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
            Text("View All", fontSize = 12.ssp, fontWeight = FontWeight.Bold, color = LightBlueTeal)
        }
        Spacer(modifier = Modifier.height(12.sdp))
        
        CommunityPost(
            name = "Omar Hassan",
            time = "2h ago",
            postBody = "Just completed my first Taraweeh prayer. The peace and tranquility is unmatched. May Allah accept our worship. \uD83E\uDD32",
            likes = "24",
            comments = "8"
        )
        Spacer(modifier = Modifier.height(10.sdp))
        CommunityPost(
            name = "Fatima Ali",
            time = "5h ago",
            postBody = "Reminder: Don't forget to make dua during the last third of the night. It's the most blessed time! ✨",
            likes = "42",
            comments = "15"
        )
    }
}

@Composable
fun CommunityPost(name: String, time: String, postBody: String, likes: String, comments: String) {
    Card(
        shape = RoundedCornerShape(16.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp)
    ) {
        Column(modifier = Modifier.padding(14.sdp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier.size(38.sdp).background(Color.Gray.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(18.sdp)
                    )
                }
                Spacer(modifier = Modifier.width(10.sdp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(name, fontWeight = FontWeight.Bold, fontSize = 13.ssp, color = DarkBlueNavy)
                        Spacer(modifier = Modifier.width(6.sdp))
                        Text(time, fontSize = 10.ssp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(4.sdp))
                    Text(postBody, fontSize = 12.ssp, color = Color.DarkGray, lineHeight = 16.ssp)
                }
            }
            Spacer(modifier = Modifier.height(10.sdp))
            Row(
                modifier = Modifier.padding(start = 48.sdp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_favorite),
                        contentDescription = null,
                        modifier = Modifier.size(14.sdp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.sdp))
                    Text(likes, fontSize = 11.ssp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(20.sdp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_comment),
                        contentDescription = null,
                        modifier = Modifier.size(14.sdp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.sdp))
                    Text(comments, fontSize = 11.ssp, color = Color.Gray)
                }
            }
        }
    }
}
