package com.webinane.salam.ui.qibla.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.webinane.salam.R
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ActionButtonsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.sdp)) {
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.sdp),
            shape = RoundedCornerShape(12.sdp),
            colors = ButtonDefaults.buttonColors(containerColor = LightBlueTeal)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_place),
                contentDescription = null,
                modifier = Modifier.size(18.sdp)
            )
            Spacer(modifier = Modifier.width(8.sdp))
            Text(text = "Locate Qibla", fontSize = 12.ssp, fontWeight = FontWeight.SemiBold)
        }
        
        Row(horizontalArrangement = Arrangement.spacedBy(10.sdp)) {
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .height(44.sdp),
                shape = RoundedCornerShape(12.sdp),
                border = androidx.compose.foundation.BorderStroke(2.dp, LightBlueTeal),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = LightBlueTeal)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_refresh),
                    contentDescription = null,
                    modifier = Modifier.size(14.sdp)
                )
                Spacer(modifier = Modifier.width(6.sdp))
                Text(text = "Recalibrate", fontSize = 10.ssp, fontWeight = FontWeight.SemiBold)
            }
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .weight(1f)
                    .height(44.sdp),
                shape = RoundedCornerShape(12.sdp),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color.Gray.copy(alpha = 0.3f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkBlueNavy)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_route),
                    contentDescription = null,
                    modifier = Modifier.size(14.sdp)
                )
                Spacer(modifier = Modifier.width(6.sdp))
                Text(text = "Map View", fontSize = 10.ssp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
