package com.webinane.salam.ui.admin.components

import androidx.compose.foundation.BorderStroke
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
import com.webinane.salam.ui.theme.BorderGray
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.ScaffoldBackground
import com.webinane.salam.ui.theme.ZakatAmber
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun AdminNotesSection(
    notes: String,
    onNotesChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.sdp, vertical = 8.sdp),
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.sdp, BorderGray)
    ) {
        Column(modifier = Modifier.padding(16.sdp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(id = R.drawable.ic_edit), contentDescription = null, tint = ZakatAmber, modifier = Modifier.size(16.sdp))
                Spacer(modifier = Modifier.width(8.sdp))
                Text("Admin Notes", fontSize = 12.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
            }
            Spacer(modifier = Modifier.height(12.sdp))
            OutlinedTextField(
                value = notes,
                onValueChange = onNotesChange,
                placeholder = { Text("Add any notes about this schedule (optional)...", fontSize = 10.ssp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.sdp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = ScaffoldBackground,
                    unfocusedBorderColor = BorderGray
                )
            )
            Text("These notes are visible to other admins only", fontSize = 10.ssp, color = Color.Gray, modifier = Modifier.padding(top = 8.sdp))
        }
    }
}
