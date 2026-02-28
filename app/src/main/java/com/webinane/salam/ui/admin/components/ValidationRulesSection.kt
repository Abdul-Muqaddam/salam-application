package com.webinane.salam.ui.admin.components

import androidx.compose.foundation.BorderStroke
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
import com.webinane.salam.ui.theme.BorderGray
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.GreenText
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun ValidationRulesSection() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.sdp, vertical = 8.sdp),
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.sdp, BorderGray)
    ) {
        Column(modifier = Modifier.padding(16.sdp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(id = R.drawable.ic_info), contentDescription = null, tint = GreenText, modifier = Modifier.size(16.sdp))
                Spacer(modifier = Modifier.width(8.sdp))
                Text("Validation Rules", fontSize = 12.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
            }
            Spacer(modifier = Modifier.height(12.sdp))
            ValidationRuleItem("Jamat time must be after Start time")
            ValidationRuleItem("Minimum 5 minutes gap recommended")
            ValidationRuleItem("All prayer times are required")
            ValidationRuleItem("Changes take effect immediately")
        }
    }
}

@Composable
fun ValidationRuleItem(text: String) {
    Row(modifier = Modifier.padding(vertical = 4.sdp)) {
        Icon(painter = painterResource(id = R.drawable.ic_mosque), contentDescription = null, tint = GreenText, modifier = Modifier.size(14.sdp).padding(top = 2.sdp))
        Spacer(modifier = Modifier.width(8.sdp))
        Text(text, fontSize = 11.ssp, color = Color.Gray)
    }
}
