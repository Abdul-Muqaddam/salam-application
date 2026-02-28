package com.webinane.salam.ui.dua.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.webinane.salam.R
import com.webinane.salam.domain.model.dua.Dua
import com.webinane.salam.ui.theme.*
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun DuaCard(
    dua: Dua,
    isBookmarked: Boolean,
    onPlayAudio: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onBookmark: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.sdp, vertical = 8.sdp), // Horizontal padding for shadow
        shape = RoundedCornerShape(16.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.sdp)
    ) {
        Column(
            modifier = Modifier.padding(16.sdp)
        ) {
            // Header with Type and Bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(LightBlueTeal.copy(alpha = 0.1f), CircleShape)
                            .padding(8.sdp)
                    ) {
                        val iconRes = when (dua.categoryId) {
                            "morning_evening" -> if (dua.type == "Evening") R.drawable.ic_moon else R.drawable.ic_sun
                            "food" -> R.drawable.ic_utensils
                            "family" -> R.drawable.ic_heart
                            "forgiveness" -> R.drawable.ic_hand_holding_heart
                            "knowledge" -> R.drawable.ic_graduation_cap
                            "travel" -> R.drawable.ic_route
                            "sickness" -> R.drawable.ic_heart
                            "mosque" -> R.drawable.ic_mosque
                            "anxiety" -> R.drawable.ic_warning
                            else -> R.drawable.ic_sun
                        }
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            tint = LightBlueTeal,
                            modifier = Modifier.size(14.sdp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.sdp))
                    Text(
                        text = dua.type.uppercase(),
                        fontSize = 10.ssp,
                        fontWeight = FontWeight.Bold,
                        color = LightBlueTeal
                    )
                }

                IconButton(onClick = onBookmark, modifier = Modifier.size(32.sdp)) {
                    Icon(
                        painter = painterResource(id = if (isBookmarked) R.drawable.ic_bookmark else R.drawable.ic_bookmark_outline),
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) LightBlueTeal else Color.LightGray,
                        modifier = Modifier.size(18.sdp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.sdp))

            // Arabic Text Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LightBlueTeal.copy(alpha = 0.05f), RoundedCornerShape(12.sdp))
                    .padding(16.sdp)
            ) {
                Text(
                    text = dua.arabicText,
                    fontSize = 20.ssp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(),
                    lineHeight = 30.ssp,
                    color = DarkBlueNavy
                )
            }

            Spacer(modifier = Modifier.height(12.sdp))

            // Transliteration & Translation
            Column(verticalArrangement = Arrangement.spacedBy(8.sdp)) {
                Column {
                    Text(text = "Transliteration:", fontSize = 10.ssp, fontWeight = FontWeight.Medium, color = Color.Gray)
                    Text(text = dua.transliteration, fontSize = 12.ssp, fontStyle = FontStyle.Italic, color = DarkBlueNavy)
                }
                Column {
                    Text(text = "Translation:", fontSize = 10.ssp, fontWeight = FontWeight.Medium, color = Color.Gray)
                    Text(text = dua.translation, fontSize = 12.ssp, color = DarkBlueNavy)
                }
            }

            Spacer(modifier = Modifier.height(16.sdp))

            // Footer Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onPlayAudio,
                    shape = RoundedCornerShape(8.sdp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightBlueTeal.copy(alpha = 0.1f),
                        contentColor = LightBlueTeal
                    ),
                    modifier = Modifier.height(32.sdp),
                    contentPadding = PaddingValues(horizontal = 12.sdp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(id = R.drawable.ic_play_arrow), contentDescription = null, modifier = Modifier.size(12.sdp))
                        Spacer(modifier = Modifier.width(6.sdp))
                        Text("Play Audio", fontSize = 10.ssp)
                    }
                }

                Row {
                    IconButton(onClick = onCopy) {
                        Icon(painter = painterResource(id = R.drawable.ic_copy), contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.sdp))
                    }
                    IconButton(onClick = onShare) {
                        Icon(painter = painterResource(id = R.drawable.ic_share_nodes), contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.sdp))
                    }
                }
            }
        }
    }
}
