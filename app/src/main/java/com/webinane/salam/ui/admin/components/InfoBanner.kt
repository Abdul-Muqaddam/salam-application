package com.webinane.salam.ui.admin.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueBackground
import com.webinane.salam.ui.theme.LightBlueHighlight
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun InfoBanner() {
    Box(modifier = Modifier.padding(16.sdp)) {
        Card(
            shape = RoundedCornerShape(12.sdp),
            colors = CardDefaults.cardColors(containerColor = LightBlueBackground),
            border = BorderStroke(1.sdp, LightBlueHighlight)
        ) {
            Row(modifier = Modifier.padding(14.sdp)) {
                Icon(painter = painterResource(id = R.drawable.ic_info), contentDescription = null, tint = LightBlueTeal, modifier = Modifier.size(16.sdp))
                Spacer(modifier = Modifier.width(12.sdp))
                Column {
                    Text("Important Notice", fontSize = 11.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
                    Text(
                        "Times remain the same daily unless updated by admin. Changes take effect immediately after saving.",
                        fontSize = 10.ssp,
                        color = LightBlueTeal,
                        lineHeight = 14.ssp
                    )
                }
            }
        }
    }
}
