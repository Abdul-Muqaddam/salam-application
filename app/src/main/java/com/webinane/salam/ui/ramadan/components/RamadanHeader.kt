package com.webinane.salam.ui.ramadan.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.ui.ramadan.HijriDateInfo
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RamadanHeader(hijriInfo: HijriDateInfo, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.sdp, vertical = 20.sdp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderButton(icon = R.drawable.ic_menu, onClick = {})
            
            Icon(
                painter = painterResource(id = R.drawable.ic_moon),
                contentDescription = null,
                tint = LightBlueTeal,
                modifier = Modifier.size(20.sdp)
            )
            
            HeaderButton(icon = R.drawable.ic_notifications, onClick = {}) 
        }
        
        Spacer(modifier = Modifier.height(15.sdp))
        
        val gregorianDate = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy", java.util.Locale.getDefault()))
        
        Text(
            text = hijriInfo.formattedFull,
            fontSize = 20.ssp,
            fontWeight = FontWeight.Bold,
            color = DarkBlueNavy
        )
        Text(
            text = gregorianDate,
            fontSize = 12.ssp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
    }
}

@Composable
fun HeaderButton(icon: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier.size(40.sdp),
        shape = RoundedCornerShape(10.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier.size(18.sdp)
            )
        }
    }
}
