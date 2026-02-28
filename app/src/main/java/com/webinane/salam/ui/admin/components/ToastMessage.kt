package com.webinane.salam.ui.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.ui.theme.GreenText
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ToastMessage(
    message: String,
    subtitle: String,
    isError: Boolean,
    onClose: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.sdp),
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = if (isError) Color.Red else GreenText)
    ) {
        Row(modifier = Modifier.padding(14.sdp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(32.sdp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = if (isError) R.drawable.ic_warning else R.drawable.ic_check),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.sdp)
                )
            }
            Spacer(modifier = Modifier.width(12.sdp))
            Column(modifier = Modifier.weight(1f)) {
                Text(message, color = Color.White, fontSize = 12.ssp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Color.White.copy(alpha = 0.9f), fontSize = 10.ssp)
            }
            IconButton(onClick = onClose) {
                Icon(painter = painterResource(id = R.drawable.ic_close), contentDescription = "Close", tint = Color.White, modifier = Modifier.size(16.sdp))
            }
        }
    }
}
