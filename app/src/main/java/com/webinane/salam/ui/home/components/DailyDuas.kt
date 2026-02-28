package com.webinane.salam.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.webinane.salam.R
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import androidx.compose.ui.res.painterResource
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import com.webinane.salam.util.TtsManager
import org.koin.compose.koinInject
@Composable
fun DailyDuas(
    bookmarkedDuas: List<com.webinane.salam.domain.model.dua.Dua> = emptyList(),
    onViewMore: () -> Unit = {},
    ttsManager: TtsManager = koinInject()
) {
    val defaultDua = com.webinane.salam.domain.model.dua.Dua(
        id = "default",
        title = "Morning Dua",
        arabicText = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ",
        transliteration = "Asbahna wa asbahal-mulku lillah",
        translation = "We have entered a new day and with it all dominion belongs to Allah.",
        type = "Morning",
        categoryId = "morning_evening"
    )

    val displayList = if (bookmarkedDuas.isEmpty()) listOf(defaultDua) else bookmarkedDuas
    val pagerState = rememberPagerState(pageCount = { displayList.size })
    
    Column(modifier = Modifier.padding(16.sdp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Daily Duas", fontSize = 14.ssp, fontWeight = FontWeight.Bold, color = LightBlueTeal)
            Text(
                text = "View More", 
                fontSize = 10.ssp, 
                color = LightBlueTeal,
                modifier = Modifier.clickable { onViewMore() }
            )
        }

        Spacer(modifier = Modifier.height(12.sdp))

        androidx.compose.foundation.pager.HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 4.sdp)
        ) { page ->
            val dua = displayList[page]
            DuaPagerItem(
                dua = dua,
                isBookmarked = bookmarkedDuas.any { it.id == dua.id },
                onViewMore = onViewMore,
                ttsManager = ttsManager
            )
        }

        if (displayList.size > 1) {
            Spacer(modifier = Modifier.height(8.sdp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(8.sdp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(displayList.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) LightBlueTeal else LightBlueTeal.copy(alpha = 0.2f)
                    Box(
                        modifier = Modifier
                            .padding(2.sdp)
                            .background(color, CircleShape)
                            .size(6.sdp)
                    )
                }
            }
        }
    }
}

@Composable
fun DuaPagerItem(
    dua: com.webinane.salam.domain.model.dua.Dua,
    isBookmarked: Boolean,
    onViewMore: () -> Unit,
    ttsManager: TtsManager
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    
    Card(
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.sdp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.sdp, vertical = 4.sdp)
            .clickable { onViewMore() }
    ) {
        Column(modifier = Modifier.padding(16.sdp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val iconRes = when (dua.categoryId) {
                        "morning_evening" -> if (dua.type.contains("Evening", ignoreCase = true)) R.drawable.ic_moon else R.drawable.ic_sun
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
                        modifier = Modifier.size(20.sdp)
                    )
                    Spacer(modifier = Modifier.width(8.sdp))
                    Column {
                        Text(
                            text = if (isBookmarked) "Your Favorite Dua" else "${dua.type} Dua",
                            fontSize = 12.ssp,
                            fontWeight = FontWeight.Bold
                        )
                        if (isBookmarked) {
                            Text(text = dua.title, fontSize = 9.ssp, color = LightBlueTeal)
                        }
                    }
                }
                
                if (isBookmarked) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark),
                        contentDescription = "Favorite",
                        tint = LightBlueTeal,
                        modifier = Modifier.size(14.sdp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.sdp))
            Box(modifier = Modifier.fillMaxWidth().background(LightBlueTeal.copy(alpha = 0.05f), RoundedCornerShape(8.sdp)).padding(12.sdp)) {
                Text(
                    text = dua.arabicText,
                    fontSize = 18.ssp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(),
                    color = DarkBlueNavy
                )
            }
            Spacer(modifier = Modifier.height(8.sdp))
            Text(text = dua.translation, fontSize = 10.ssp, color = Color.Gray)
            
            Spacer(modifier = Modifier.height(12.sdp))
            
            // Listen Button
            Button(
                onClick = {
                    if (ttsManager.isReady()) {
                        ttsManager.speak(dua.arabicText)
                    } else {
                        android.widget.Toast.makeText(context, "Voice engine is initializing...", android.widget.Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.sdp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightBlueTeal.copy(alpha = 0.1f),
                    contentColor = LightBlueTeal
                ),
                shape = RoundedCornerShape(8.sdp),
                contentPadding = PaddingValues(0.sdp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_listen),
                        contentDescription = null,
                        modifier = Modifier.size(16.sdp)
                    )
                    Spacer(modifier = Modifier.width(8.sdp))
                    Text(
                        text = "Listen",
                        fontSize = 11.ssp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
