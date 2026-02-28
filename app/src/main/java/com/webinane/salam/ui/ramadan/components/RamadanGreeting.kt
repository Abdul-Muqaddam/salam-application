package com.webinane.salam.ui.ramadan.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.ui.ramadan.HijriDateInfo
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RamadanGreeting(userName: String, hijriInfo: HijriDateInfo, onRenameClick: () -> Unit) {
    val welcomeText = if (hijriInfo.isRamadan) "Ramadan Mubarak," else "Assalamu Alaikum,"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.sdp, vertical = 10.sdp)
            .background(
                brush = Brush.linearGradient(listOf(LightBlueTeal, com.webinane.salam.ui.theme.DarkTealGradient)),
                shape = RoundedCornerShape(16.sdp)
            )
            .padding(16.sdp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.sdp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_user),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.sdp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.sdp))
                    Column (modifier = Modifier.fillMaxWidth()){
                        Text(welcomeText, color = Color.White.copy(alpha = 0.8f), fontSize = 11.ssp)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(userName, color = Color.White, fontSize = 16.ssp, fontWeight = FontWeight.Bold)
                            Icon(
                                painter = painterResource(id = R.drawable.ic_edit), // Assuming ic_edit exists, if not use ic_pencil or similar
                                contentDescription = "Edit Name",
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier
                                    .size(14.sdp)
                                    .clickable { onRenameClick() }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.sdp))
            Text(
                "May this blessed month bring peace and prosperity to you and your loved ones",
                color = Color.White,
                fontSize = 12.ssp,
                lineHeight = 18.ssp
            )
        }
    }
}
