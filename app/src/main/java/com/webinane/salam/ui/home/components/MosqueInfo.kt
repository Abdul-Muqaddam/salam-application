package com.webinane.salam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import com.webinane.salam.R
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun MosqueInfo() {
    Column(modifier = Modifier.padding(16.sdp)) {
        Text(text = stringResource(R.string.nearby_mosque), fontSize = 14.ssp, fontWeight = FontWeight.Bold, color = LightBlueTeal)
        Spacer(modifier = Modifier.height(10.sdp))
        Card(
            shape = RoundedCornerShape(12.sdp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(12.sdp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(45.sdp)
                        .background(LightBlueTeal, RoundedCornerShape(10.sdp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_location), contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.sdp))
                Column {
                    Text(text = stringResource(R.string.mosque_name_placeholder), fontSize = 13.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
                    Text(text = stringResource(R.string.mosque_location_placeholder), fontSize = 10.ssp, color = Color.Gray)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_star), contentDescription = null, tint = LightBlueTeal, modifier = Modifier.size(12.sdp))
                        Text(text = stringResource(R.string.mosque_reviews_placeholder), fontSize = 10.ssp, color = Color.Gray)
                    }
                }
            }
        }
    }
}
