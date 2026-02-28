package com.webinane.salam.ui.admin.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.ui.theme.BorderGray
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun StickyFooter(onSave: () -> Unit, onReset: () -> Unit, isLoading: Boolean = false) {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.sdp
    ) {
        Column(modifier = Modifier.padding(16.sdp).navigationBarsPadding()) {
            Button(
                onClick = onSave,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(48.sdp),
                colors = ButtonDefaults.buttonColors(containerColor = LightBlueTeal),
                shape = RoundedCornerShape(12.sdp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.sdp),
                        color = Color.White,
                        strokeWidth = 2.sdp
                    )
                } else {
                    Icon(painter = painterResource(id = R.drawable.ic_clock), contentDescription = null, modifier = Modifier.size(18.sdp), tint = Color.White)
                    Spacer(modifier = Modifier.width(8.sdp))
                    Text("Save Prayer Schedule", fontSize = 12.ssp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.sdp))
            OutlinedButton(
                onClick = onReset,
                modifier = Modifier.fillMaxWidth().height(44.sdp),
                border = BorderStroke(1.sdp, BorderGray),
                shape = RoundedCornerShape(12.sdp)
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_refresh), contentDescription = null, modifier = Modifier.size(16.sdp), tint = DarkBlueNavy)
                Spacer(modifier = Modifier.width(8.sdp))
                Text("Reset to Previous", fontSize = 12.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
            }
        }
    }
}
