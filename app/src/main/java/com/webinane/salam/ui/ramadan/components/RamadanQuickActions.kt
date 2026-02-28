package com.webinane.salam.ui.ramadan.components

import androidx.compose.foundation.background
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
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun RamadanQuickActions(onActionClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 20.sdp, vertical = 15.sdp)) {
        Text("Quick Actions", fontSize = 16.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
        Spacer(modifier = Modifier.height(12.sdp))
        
        val actions = listOf(
            Triple("Quran", R.drawable.ic_quran, "Quran"),
            Triple("Dua", R.drawable.ic_duas, "Dua"),
            Triple("Qibla", R.drawable.ic_place, "Qibla"),
            Triple("Zakat", R.drawable.ic_hand_holding_heart, "Zakat")
        )
        
        Column(verticalArrangement = Arrangement.spacedBy(8.sdp)) {
            for (i in 0 until actions.size step 3) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.sdp)
                ) {
                    for (j in i until (i + 3)) {
                        if (j < actions.size) {
                            val action = actions[j]
                            RamadanActionItem(
                                modifier = Modifier.weight(1f),
                                title = action.first,
                                icon = action.second,
                                onClick = { onActionClick(action.first) }
                            )
                        } else {
                            // Empty box for alignment
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RamadanActionItem(modifier: Modifier, title: String, icon: Int, onClick: () -> Unit) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(12.sdp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(42.sdp)
                    .background(LightBlueTeal.copy(alpha = 0.1f), RoundedCornerShape(10.sdp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = LightBlueTeal,
                    modifier = Modifier.size(20.sdp)
                )
            }
            Spacer(modifier = Modifier.height(6.sdp))
            Text(title, fontSize = 11.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
        }
    }
}
