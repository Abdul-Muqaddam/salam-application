package com.webinane.salam.ui.dua

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.ui.dua.components.DuaCard
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import com.webinane.salam.ui.theme.ScaffoldBackground
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import com.webinane.salam.util.TtsManager
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuaScreen(
    onNavigateBack: () -> Unit
) {
    val viewModel: DuaViewModel = koinViewModel()
    val ttsManager: TtsManager = koinInject()
    val context = LocalContext.current
    val categories by viewModel.categories.collectAsState()
    val duas by viewModel.duas.collectAsState()
    val bookmarkedDua by viewModel.bookmarkedDua.collectAsState()
    val bookmarkedDuaIds by viewModel.bookmarkedDuaIds.collectAsState()
    val selectedCategoryName by viewModel.selectedCategoryName.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Duas & Supplications", fontWeight = FontWeight.Bold, fontSize = 18.ssp) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScaffoldBackground,
                    titleContentColor = DarkBlueNavy
                )
            )
        },
        containerColor = ScaffoldBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Horizontal Categories
            item {
                Column(modifier = Modifier.padding(top = 16.sdp, bottom = 8.sdp)) {
                    Text(
                        text = "Categories",
                        fontSize = 15.ssp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlueNavy,
                        modifier = Modifier.padding(start = 16.sdp, end = 16.sdp, bottom = 12.sdp)
                    )
                    
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.sdp),
                        horizontalArrangement = Arrangement.spacedBy(8.sdp)
                    ) {
                        items(categories) { category ->
                            FilterChip(
                                selected = selectedCategoryName == category.name,
                                onClick = { viewModel.loadDuasByCategory(category.id) },
                                label = { Text(category.name, fontSize = 11.ssp) },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = category.iconRes),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.sdp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = LightBlueTeal,
                                    selectedLabelColor = Color.White,
                                    selectedLeadingIconColor = Color.White,
                                    labelColor = DarkBlueNavy,
                                    iconColor = LightBlueTeal
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = LightBlueTeal.copy(alpha = 0.2f),
                                    enabled = true,
                                    selected = selectedCategoryName == category.name
                                )
                            )
                        }
                    }
                }
            }

            // Morning & Evening List Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.sdp, vertical = 8.sdp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedCategoryName,
                        fontSize = 16.ssp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlueNavy
                    )
                    IconButton(onClick = { /* Filter */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = "Filter",
                            tint = LightBlueTeal,
                            modifier = Modifier.size(18.sdp)
                        )
                    }
                }
            }

            // Dua List
            items(duas) { dua ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.sdp)
                ) {
                    DuaCard(
                        dua = dua,
                        isBookmarked = bookmarkedDuaIds.contains(dua.id),
                        onPlayAudio = {
                            if (ttsManager.isReady()) {
                                val success = ttsManager.speak(dua.arabicText)
                                if (!success) {
                                    Toast.makeText(context, "Could not play audio", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Voice engine is initializing...", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onCopy = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Dua", "${dua.arabicText}\n\n${dua.translation}")
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        onShare = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                val promoText = "\nWanna learn or know more Dua? Click the link down below:\nhttps://play.google.com/store/apps/details?id=${context.packageName}"
                                putExtra(Intent.EXTRA_TEXT, "${dua.title}\n\n${dua.arabicText}\n\n${dua.translation}\n$promoText")
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        },
                        onBookmark = { 
                            val isCurrentlyBookmarked = bookmarkedDuaIds.contains(dua.id)
                            viewModel.toggleBookmark(dua.id)
                            val message = if (isCurrentlyBookmarked) "Removed from bookmarks" else "Saved to bookmarks"
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(20.sdp))
            }
        }
    }
}
