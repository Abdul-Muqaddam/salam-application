package com.webinane.salam.ui.quran

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.webinane.salam.R
import com.webinane.salam.data.remote.model.quran.AyahDto
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranReaderScreen(
    surahNumber: Int,
    surahName: String,
    onNavigateBack: () -> Unit,
    viewModel: QuranViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val playingUrl by viewModel.currentPlayingUrl.collectAsState()

    LaunchedEffect(surahNumber) {
        viewModel.loadSurahDetails(surahNumber)
    }

    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
    
    LaunchedEffect(state.activeAyahIndex) {
        if (state.activeAyahIndex >= 0) {
            val targetIndex = state.activeAyahIndex + 1 // +1 for Bismillah header
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            val itemInfo = visibleItems.find { it.index == targetIndex }
            
            if (itemInfo != null) {
                // Check if fully visible
                val itemTop = itemInfo.offset
                val itemBottom = itemInfo.offset + itemInfo.size
                val viewportStart = layoutInfo.viewportStartOffset
                val viewportEnd = layoutInfo.viewportEndOffset
                
                // Allow some tolerance? e.g. 10px
                val isTopVisible = itemTop >= viewportStart - 50
                val isBottomVisible = itemBottom <= viewportEnd + 50
                
                if (isTopVisible && isBottomVisible) {
                    return@LaunchedEffect
                }
            }
            
            // If not fully visible, scroll nicely.
            // Scroll to the previous item to give context (smooth flow) instead of snapping target to top.
            // But ensure target is at least appearing.
            listState.animateScrollToItem(kotlin.math.max(0, targetIndex - 1))
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = surahName,
                        fontSize = 18.ssp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlueNavy
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Back",
                            tint = DarkBlueNavy
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.sdp)
            ) {
                if (state.activeAyahIndex >= 0 || state.isPlaying) {
                    SmallFloatingActionButton(
                        onClick = { viewModel.stopAudio() },
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_stop), // Ensure ic_stop exists or use existing
                            contentDescription = "Stop"
                        )
                    }
                }

                ExtendedFloatingActionButton(
                    onClick = { 
                        viewModel.togglePlayPause()
                    },
                    containerColor = LightBlueTeal,
                    contentColor = Color.White,
                    icon = {
                        Icon(
                            painter = painterResource(
                                id = if (state.isPlaying) R.drawable.ic_pause else R.drawable.ic_play_arrow
                            ),
                            contentDescription = null
                        )
                    },
                    text = {
                        val text = when {
                            state.isPlaying -> "Pause Surah"
                            state.activeAyahIndex >= 0 -> "Resume Surah"
                            else -> "Play Entire Surah"
                        }
                        Text(text = text)
                    }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
                .padding(padding)
        ) {
            if (state.isLoadingDetails) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = LightBlueTeal
                )
            } else if (state.detailsError != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Error: ${state.detailsError}", color = Color.Red)
                    Button(onClick = { viewModel.loadSurahDetails(surahNumber) }) {
                        Text("Retry")
                    }
                }
            } else {
                state.currentSurah?.let { surah ->
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(16.sdp)
                    ) {
                        // Bismillah
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.sdp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                                    fontSize = 24.ssp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkBlueNavy
                                )
                            }
                        }
                        
                        itemsIndexed(surah.ayahs) { index, ayah ->
                            val isHighlighted = index == state.activeAyahIndex
                            val isItemPlaying = isHighlighted && state.isPlaying
                            
                            AyahItem(
                                ayah = ayah,
                                isPlaying = isItemPlaying,
                                isHighlighted = isHighlighted,
                                onPlayClick = { url -> viewModel.toggleAudio(url) }
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.sdp), color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AyahItem(
    ayah: AyahDto,
    isPlaying: Boolean, // Kept for individual button state
    isHighlighted: Boolean,
    onPlayClick: (String?) -> Unit
) {
    val parts = ayah.text.split("|||")
    val arabicText = parts.getOrNull(0) ?: ayah.text
    val translationText = parts.getOrNull(1) ?: ""

    val targetBackgroundColor = if (isHighlighted) LightBlueTeal.copy(alpha = 0.1f) else Color.Transparent
    val backgroundColor by animateColorAsState(targetBackgroundColor, label = "bgColor")
    val padding by animateDpAsState(if (isHighlighted) 8.sdp else 0.sdp, label = "padding")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, androidx.compose.foundation.shape.RoundedCornerShape(8.sdp))
            .padding(padding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Play Button
            IconButton(
                onClick = { onPlayClick(ayah.audio) },
                modifier = Modifier
                    .size(32.sdp)
                    .background(LightBlueTeal.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isPlaying) R.drawable.ic_stop else R.drawable.ic_play_arrow
                    ),
                    contentDescription = if (isPlaying) "Stop" else "Play",
                    tint = LightBlueTeal,
                    modifier = Modifier.size(20.sdp)
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Verse Number
            Text(
                text = "Verse ${ayah.numberInSurah}",
                fontSize = 10.ssp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.sdp))

        // Arabic
        Text(
            text = arabicText,
            fontSize = 22.ssp,
            fontWeight = FontWeight.Bold,
            color = DarkBlueNavy,
            textAlign = TextAlign.Right,
            lineHeight = 36.ssp,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(12.sdp))
        
        // Translation
        if (translationText.isNotEmpty()) {
            Text(
                text = translationText,
                fontSize = 14.ssp,
                color = Color.DarkGray,
                textAlign = TextAlign.Left,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
