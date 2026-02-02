package com.webinane.salam.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.webinane.salam.ui.home.components.*
import com.webinane.salam.ui.theme.ScaffoldBackground
import com.webinane.salam.ui.viewmodel.PrayerTimesViewModel
import ir.kaaveh.sdpcompose.sdp
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onNavigatePrayerTimes: () -> Unit,
    onNavigateNotifications: () -> Unit,
    onNavigateZakat: () -> Unit,
    onNavigateQibla: () -> Unit = {},
    viewModel: PrayerTimesViewModel = koinViewModel()
) {
    val prayerTiming by viewModel.prayerTiming.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()
    val currentPrayerName by viewModel.highlightedPrayer.collectAsState()
    val countdownTimer by viewModel.countdownTimer.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(onNavigateNotifications)
        },
        containerColor = ScaffoldBackground // Light background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                HomeHeader(currentTime, currentDate, onNavigateNotifications)
            }

            item {
                CurrentPrayerCard(
                    prayerName = currentPrayerName,
                    time = prayerTiming?.let {
                        when(currentPrayerName) {
                            "FAJR" -> it.fajr.start
                            "DHUHR" -> it.dhuhr.start
                            "ASR" -> it.asr.start
                            "MAGHRIB" -> it.maghrib.start
                            "ISHA" -> it.isha.start
                            else -> "--:--"
                        }
                    } ?: "--:--",
                    jamaat = prayerTiming?.let {
                        when(currentPrayerName) {
                            "FAJR" -> it.fajr.jamaat
                            "DHUHR" -> it.dhuhr.jamaat
                            "ASR" -> it.asr.jamaat
                            "MAGHRIB" -> it.maghrib.jamaat
                            "ISHA" -> it.isha.jamaat
                            else -> "--:--"
                        }
                    } ?: "--:--",
                    countdown = countdownTimer,
                    onSeeAll = onNavigatePrayerTimes
                )
            }

            item {
                QuickActions(
                    onNavigateZakat = onNavigateZakat,
                    onNavigateQibla = onNavigateQibla
                )
            }

            item {
                PrayerList(
                    prayerTiming = prayerTiming,
                    currentPrayer = currentPrayerName,
                    onSeeAll = onNavigatePrayerTimes
                )
            }

            item {
                MosqueInfo()
            }

            item {
                Announcements()
            }
            
            item {
                IslamicCalendar()
            }
            
            item {
                DhikrCounter()
            }
            
            item {
                DailyDuas()
            }
            
            item {
                CommunityStats()
            }

            item {
                Spacer(modifier = Modifier.height(padding.calculateBottomPadding() + 20.sdp))
            }
        }
    }
}
