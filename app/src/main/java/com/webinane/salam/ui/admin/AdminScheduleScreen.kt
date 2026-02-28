package com.webinane.salam.ui.admin

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import com.webinane.salam.R
import com.webinane.salam.ui.theme.*
import com.webinane.salam.ui.admin.components.*
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScheduleScreen(
    viewModel: AdminScheduleViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    if (state.isLoadingInitialData || state.isLoadingGpsCalculation) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ScaffoldBackground),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = LightBlueTeal,
                strokeWidth = 3.sdp,
                modifier = Modifier.size(48.sdp)
            )
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Admin Panel", fontSize = 16.ssp, fontWeight = FontWeight.SemiBold, color = DarkBlueNavy) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "Back", tint = DarkBlueNavy)
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Menu */ }) {
                            Icon(painter = painterResource(id = R.drawable.ic_menu), contentDescription = "Menu", tint = DarkBlueNavy)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            bottomBar = {
                StickyFooter(
                    onSave = { viewModel.saveSchedule() },
                    onReset = { viewModel.resetSchedule() },
                    isLoading = state.isLoading
                )
            },
            containerColor = ScaffoldBackground // bg-gray-50
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(scrollState)
                ) {
                    AdminPageHeader()
                    InfoBanner()
                    ScheduleStatusSection(lastUpdatedTimestamp = state.lastUpdatedTimestamp)
                    
                    Column(modifier = Modifier.padding(horizontal = 16.sdp, vertical = 20.sdp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Daily Prayer Times", fontSize = 14.ssp, fontWeight = FontWeight.Bold, color = DarkBlueNavy)
                            Text("All times are local", fontSize = 10.ssp, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.height(12.sdp))
                        
                        // Mode Toggle
                        ManualModeToggle(
                            isEnabled = state.isManualMode,
                            onToggle = { viewModel.onToggleManualMode(it) }
                        )
                        
                        Spacer(modifier = Modifier.height(12.sdp))

                        PrayerCard(
                            name = "Fajr",
                            subtitle = "Dawn Prayer",
                            icon = R.drawable.ic_fajr,
                            startTime = state.fajrStart,
                            jamatTime = state.fajrJamat,
                            onStartTimeChange = { viewModel.onTimeChange("fajr", "start", it) },
                            onJamatTimeChange = { viewModel.onTimeChange("fajr", "jamat", it) },
                            showWarning = true,
                            warningText = "15 min gap recommended",
                            enabled = state.isManualMode
                        )
                        
                        PrayerCard(
                            name = "Dhuhr",
                            subtitle = "Noon Prayer",
                            icon = R.drawable.ic_dhuhr,
                            startTime = state.dhuhrStart,
                            jamatTime = state.dhuhrJamat,
                            onStartTimeChange = { viewModel.onTimeChange("dhuhr", "start", it) },
                            onJamatTimeChange = { viewModel.onTimeChange("dhuhr", "jamat", it) },
                            enabled = state.isManualMode
                        )

                        // Jummah - Always enabled or follows manual mode? usually fixed.
                        // Let's keep it editable always for now, or match existing manual mode logic.
                        // Since user asked for "Jummah", it implies it might be different from Dhuhr.
                        PrayerCard(
                            name = "Jummah",
                            subtitle = "Friday Prayer",
                            icon = R.drawable.ic_dhuhr, // Re-use Dhuhr or use generic
                            startTime = state.jummahStart,
                            jamatTime = state.jummahJamat,
                            onStartTimeChange = { viewModel.onJummahStartChange(it) },
                            onJamatTimeChange = { viewModel.onJummahJamatChange(it) },
                            enabled = state.isManualMode // Now follows global manual mode setting
                        )

                        PrayerCard(
                            name = "Asr",
                            subtitle = "Afternoon Prayer",
                            icon = R.drawable.ic_asr,
                            startTime = state.asrStart,
                            jamatTime = state.asrJamat,
                            onStartTimeChange = { viewModel.onTimeChange("asr", "start", it) },
                            onJamatTimeChange = { viewModel.onTimeChange("asr", "jamat", it) },
                            enabled = state.isManualMode
                        )

                        PrayerCard(
                            name = "Maghrib",
                            subtitle = "Sunset Prayer",
                            icon = R.drawable.ic_maghrib,
                            startTime = state.maghribStart,
                            jamatTime = state.maghribJamat,
                            onStartTimeChange = { viewModel.onTimeChange("maghrib", "start", it) },
                            onJamatTimeChange = { viewModel.onTimeChange("maghrib", "jamat", it) },
                            showInfo = true,
                            infoText = "Short gap typical for Maghrib",
                            enabled = state.isManualMode
                        )

                        PrayerCard(
                            name = "Isha",
                            subtitle = "Night Prayer",
                            icon = R.drawable.ic_isha,
                            startTime = state.ishaStart,
                            jamatTime = state.ishaJamat,
                            onStartTimeChange = { viewModel.onTimeChange("isha", "start", it) },
                            onJamatTimeChange = { viewModel.onTimeChange("isha", "jamat", it) },
                            enabled = state.isManualMode
                        )
                    }

                    NotificationSettingsCard(
                        adhanNotif = state.adhanNotif,
                        jamatNotif = state.jamatNotif,
                        reminderNotif = state.reminderNotif,
                        onToggle = { type, enabled -> viewModel.onToggleNotif(type, enabled) }
                    )

                    PreviewScheduleSection(state)
                    ValidationRulesSection()
                    AdminNotesSection(
                        notes = state.adminNotes,
                        onNotesChange = { viewModel.onNotesChange(it) }
                    )

                    SecuritySettingsCard(
                        oldPin = state.oldPin,
                        newPin = state.newPin,
                        confirmPin = state.confirmPin,
                        isLoading = state.pinChangeLoading,
                        onFieldChange = { field, value -> viewModel.onPinFieldChange(field, value) },
                        onUpdateClick = { viewModel.updateAdminPin() }
                    )
                    
                    Spacer(modifier = Modifier.height(60.sdp)) // Extra space for footer
                }

                // Success Toast
                AnimatedVisibility(
                    visible = state.showSuccessToast,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut(),
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 80.sdp)
                ) {
                    ToastMessage(
                        message = "Prayer schedule updated successfully",
                        subtitle = "All users will receive notifications",
                        isError = false,
                        onClose = { viewModel.dismissToasts() }
                    )
                }

                // Error Toast
                AnimatedVisibility(
                    visible = state.showErrorToast,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut(),
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 80.sdp)
                ) {
                    ToastMessage(
                        message = "Validation Error",
                        subtitle = state.errorMessage,
                        isError = true,
                        onClose = { viewModel.dismissToasts() }
                    )
                }
            }
        }
    }
}