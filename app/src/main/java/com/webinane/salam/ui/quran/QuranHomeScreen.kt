package com.webinane.salam.ui.quran

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.webinane.salam.R
import com.webinane.salam.data.remote.model.quran.SurahDto
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranHomeScreen(
    onNavigateBack: () -> Unit,
    onNavigateToReader: (Int, String) -> Unit,
    viewModel: QuranViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Header
        CenterAlignedTopAppBar(
            title = { 
                Text(
                    text = "Al-Quran",
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

        // Search Bar
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.sdp),
            placeholder = { Text("Search Surah by name or number") },
            leadingIcon = { 
                Icon(
                    painter = painterResource(id = R.drawable.ic_search), 
                    contentDescription = "Search"
                ) 
            },
            shape = RoundedCornerShape(12.sdp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LightBlueTeal,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            singleLine = true
        )

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = LightBlueTeal)
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Error: ${state.error}", color = Color.Red)
                    Button(onClick = { viewModel.fetchSurahs() }) {
                        Text("Retry")
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.sdp)
            ) {
                items(state.filteredSurahs) { surah ->
                    SurahItem(surah = surah, onClick = { onNavigateToReader(surah.number, surah.englishName) })
                }
            }
        }
    }
}

@Composable
fun SurahItem(
    surah: SurahDto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.sdp, vertical = 4.sdp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.sdp),
        shape = RoundedCornerShape(12.sdp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Number Badge
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.sdp)
                    .background(LightBlueTeal.copy(alpha = 0.1f), shape = RoundedCornerShape(8.sdp))
            ) {
                Text(
                    text = surah.number.toString(),
                    color = LightBlueTeal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.ssp
                )
            }
            
            Spacer(modifier = Modifier.width(16.sdp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = surah.englishName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.ssp,
                    color = DarkBlueNavy
                )
                Text(
                    text = "${surah.revelationType} • ${surah.numberOfAyahs} Verses",
                    fontSize = 12.ssp,
                    color = Color.Gray
                )
            }
            
            Text(
                text = surah.name, // Arabic Name
                fontSize = 18.ssp,
                fontWeight = FontWeight.Bold,
                color = LightBlueTeal,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Default // Use a simpler font if Arabic isn't rendering well
            )
        }
    }
}
