package com.webinane.salam.ui.ramadan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.webinane.salam.ui.PrayerTimesViewModel
import com.webinane.salam.ui.ramadan.components.*
import com.webinane.salam.ui.theme.DarkBlueNavy
import com.webinane.salam.ui.theme.LightBlueTeal
import com.webinane.salam.ui.theme.ScaffoldBackground
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RamadanScreen(
    onNavigateBack: () -> Unit,
    onNavigateQuran: () -> Unit,
    onNavigateDua: () -> Unit,
    onNavigateQibla: () -> Unit,
    onNavigateZakat: () -> Unit
) {
    val calendarViewModel: IslamicCalendarViewModel = koinViewModel()
    val calendarState by calendarViewModel.state.collectAsState()
    
    val prayerViewModel: PrayerTimesViewModel = koinViewModel()
    val prayerTiming by prayerViewModel.prayerTiming.collectAsState()
    val currentPrayerName by prayerViewModel.highlightedPrayer.collectAsState()
    val countdownTimer by prayerViewModel.countdownTimer.collectAsState()

    val userViewModel: UserViewModel = koinViewModel()
    val userName by userViewModel.userName.collectAsState()

    val locationRepository: com.webinane.salam.domain.repository.LocationRepository = org.koin.compose.koinInject()
    val locationName by locationRepository.getLocationName().collectAsState(initial = "New York, USA")

    var showRenameDialog by remember { mutableStateOf(false) }
    
    val hijriInfo = getCurrentHijriInfo()
    val gregorianYear = LocalDate.now().year

    Scaffold(
        containerColor = ScaffoldBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = padding)
        ) {
            // Header
            item { 
                RamadanHeader(
                    hijriInfo = hijriInfo,
                    onBack = onNavigateBack
                ) 
            }
            
            // Greeting Section
            item { 
                RamadanGreeting(
                    userName = userName.ifEmpty { "User" }, 
                    hijriInfo = hijriInfo,
                    onRenameClick = { showRenameDialog = true }
                ) 
            }
            
            // Countdown Section
            item { RamadanCountdown(hijriInfo, locationName) }
            
            // Iftar & Suhoor Cards
            item { 
                RamadanPrayerSummary(
                    hijriInfo = hijriInfo,
                    prayerTiming = prayerTiming,
                    countdown = countdownTimer
                ) 
            }
            
            // Quick Actions Grid
            item { 
                RamadanQuickActions(
                    onActionClick = { action ->
                        when (action) {
                            "Quran" -> onNavigateQuran()
                            "Dua" -> onNavigateDua()
                            "Qibla" -> onNavigateQibla()
                            "Zakat" -> onNavigateZakat()
                        }
                    }
                ) 
            }
            
            // All Prayer Times List
            item { 
                RamadanPrayerList(
                    prayerTiming = prayerTiming,
                    currentPrayer = currentPrayerName
                ) 
            }
            
            // Ramadan Progress Section
            item { RamadanProgressSection(hijriInfo) }
            
            // Hadith of the Day
            item { RamadanHadithSection() }
            
            // Community Section
            item { RamadanCommunitySection() }
            
            // Reminders Section
            item { RamadanRemindersSection() }
            
            // Islamic Calendar Section
            item { 
                RamadanCalendarSection(
                    state = calendarState,
                    todayHijriInfo = hijriInfo,
                    onPreviousMonth = { calendarViewModel.previousMonth() },
                    onNextMonth = { calendarViewModel.nextMonth() }
                ) 
            }
            
            // Tips Section
            item { RamadanTipsSection() }

            item { Spacer(modifier = Modifier.height(30.sdp)) }
        }

        if (showRenameDialog) {
            var tempName by remember { mutableStateOf("") }
            Dialog(onDismissRequest = { showRenameDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.sdp),
                    shape = RoundedCornerShape(24.sdp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.sdp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.sdp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Icon & Title
                        Box(
                            modifier = Modifier
                                .size(56.sdp)
                                .background(
                                    brush = Brush.linearGradient(listOf(LightBlueTeal, DarkBlueNavy)),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = com.webinane.salam.R.drawable.ic_user),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.sdp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.sdp))
                        
                        Text(
                            text = "Set Your Name",
                            fontSize = 18.ssp,
                            fontWeight = FontWeight.Bold,
                            color = DarkBlueNavy
                        )
                        
                        Text(
                            text = "How should we address you?",
                            fontSize = 11.ssp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.sdp)
                        )
                        
                        Spacer(modifier = Modifier.height(24.sdp))
                        
                        OutlinedTextField(
                            value = tempName,
                            onValueChange = { tempName = it },
                            placeholder = { Text("Enter your name", color = Color.Gray) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.sdp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LightBlueTeal,
                                unfocusedBorderColor = Color.LightGray,
                                focusedLabelColor = LightBlueTeal,
                                cursorColor = LightBlueTeal
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(24.sdp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.sdp)
                        ) {
                            // Cancel Button
                            TextButton(
                                onClick = { showRenameDialog = false },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.sdp)
                            ) {
                                Text("Cancel", color = Color.Gray, fontWeight = FontWeight.SemiBold)
                            }
                            
                            // Save Button
                            Button(
                                onClick = {
                                    userViewModel.updateUserName(tempName)
                                    showRenameDialog = false
                                },
                                modifier = Modifier.weight(1.5f),
                                shape = RoundedCornerShape(12.sdp),
                                colors = ButtonDefaults.buttonColors(containerColor = LightBlueTeal)
                            ) {
                                Text("Save Changes", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
