package com.webinane.salam.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webinane.salam.data.local.PrayerSchedulePreference
import com.webinane.salam.domain.repository.PrayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

data class AdminScheduleUiState(
    val isManualMode: Boolean = false,
    val fajrStart: String = "05:30",
    val fajrJamat: String = "05:45",
    val dhuhrStart: String = "13:15",
    val dhuhrJamat: String = "13:30",
    val asrStart: String = "16:45",
    val asrJamat: String = "17:00",
    val maghribStart: String = "19:20",
    val maghribJamat: String = "19:25",
    val ishaStart: String = "20:45",
    val ishaJamat: String = "21:00",
    val jummahStart: String = "13:00",
    val jummahJamat: String = "13:30",
    val adhanNotif: Boolean = true,
    val jamatNotif: Boolean = true,
    val reminderNotif: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingGpsCalculation: Boolean = false,
    val isLoadingInitialData: Boolean = true,
    val showSuccessToast: Boolean = false,
    val showErrorToast: Boolean = false,
    val errorMessage: String = "",
    val lastUpdatedTimestamp: Long? = null,
    val adminNotes: String = "",
    val hasChanges: Boolean = false,
    
    // PIN Change fields
    val oldPin: String = "",
    val newPin: String = "",
    val confirmPin: String = "",
    val pinChangeLoading: Boolean = false
)

class AdminScheduleViewModel(
    private val prefs: PrayerSchedulePreference,
    private val prayerRepository: PrayerRepository,
    private val firebaseDatabase: com.google.firebase.database.FirebaseDatabase,
    private val notificationSender: com.webinane.salam.util.NotificationSender
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminScheduleUiState())
    val uiState: StateFlow<AdminScheduleUiState> = _uiState.asStateFlow()
    
    // Track original state to detect changes for notifications
    private var originalState: AdminScheduleUiState? = null

    init {
        loadSettings()
        fetchLastUpdatedTime()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingInitialData = true) }
            
            val isManual = prefs.isManualMode
            
            // If GPS mode is active, calculate fresh GPS times
            if (!isManual) {
                try {
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                    val todayTimes = prayerRepository.calculateGpsPrayerTimes(today)
                    
                    _uiState.update {
                        it.copy(
                            isManualMode = false,
                            fajrStart = todayTimes.fajr.start,
                            fajrJamat = todayTimes.fajr.jamaat,
                            dhuhrStart = todayTimes.dhuhr.start,
                            dhuhrJamat = todayTimes.dhuhr.jamaat,
                            asrStart = todayTimes.asr.start,
                            asrJamat = todayTimes.asr.jamaat,
                            maghribStart = todayTimes.maghrib.start,
                            maghribJamat = todayTimes.maghrib.jamaat,
                            ishaStart = todayTimes.isha.start,
                            ishaJamat = todayTimes.isha.jamaat,
                            adhanNotif = prefs.adhanNotificationsEnabled,
                            jamatNotif = prefs.jamatNotificationsEnabled,
                            reminderNotif = prefs.reminderBeforeJamatEnabled,
                            isLoadingInitialData = false
                        )
                    }
                } catch (e: Exception) {
                    // Fallback to saved times if GPS calculation fails
                    _uiState.update {
                        it.copy(
                            isManualMode = prefs.isManualMode,
                            fajrStart = prefs.fajrStart,
                            fajrJamat = prefs.fajrJamat,
                            dhuhrStart = prefs.dhuhrStart,
                            dhuhrJamat = prefs.dhuhrJamat,
                            asrStart = prefs.asrStart,
                            asrJamat = prefs.asrJamat,
                            maghribStart = prefs.maghribStart,
                            maghribJamat = prefs.maghribJamat,
                            ishaStart = prefs.ishaStart,
                            ishaJamat = prefs.ishaJamat,
                            jummahStart = prefs.jummahStart,
                            jummahJamat = prefs.jummahJamat,
                            adhanNotif = prefs.adhanNotificationsEnabled,
                            jamatNotif = prefs.jamatNotificationsEnabled,
                            reminderNotif = prefs.reminderBeforeJamatEnabled,
                            isLoadingInitialData = false
                        )
                    }
                }
            } else {
                // Manual mode: load saved times
                _uiState.update {
                    it.copy(
                        isManualMode = prefs.isManualMode,
                        fajrStart = prefs.fajrStart,
                        fajrJamat = prefs.fajrJamat,
                        dhuhrStart = prefs.dhuhrStart,
                        dhuhrJamat = prefs.dhuhrJamat,
                        asrStart = prefs.asrStart,
                        asrJamat = prefs.asrJamat,
                        maghribStart = prefs.maghribStart,
                        maghribJamat = prefs.maghribJamat,
                        ishaStart = prefs.ishaStart,
                        ishaJamat = prefs.ishaJamat,
                        adhanNotif = prefs.adhanNotificationsEnabled,
                        jamatNotif = prefs.jamatNotificationsEnabled,
                        reminderNotif = prefs.reminderBeforeJamatEnabled,
                        isLoadingInitialData = false
                    )
                }
            }
            
            // Store original state after loading
            originalState = _uiState.value
        }
    }
    
    private fun fetchLastUpdatedTime() {
        viewModelScope.launch {
            try {
                val snapshot = firebaseDatabase.getReference("schedule_metadata/last_updated")
                    .get()
                    .await()
                
                val timestamp = snapshot.getValue(Long::class.java)
                _uiState.update { it.copy(lastUpdatedTimestamp = timestamp) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onToggleManualMode(enabled: Boolean) {
        _uiState.update { it.copy(isManualMode = enabled, hasChanges = true) }
        
        // Save preference immediately as requested
        prefs.isManualMode = enabled
        
        // If switching to automatic (GPS), fetch calculated times from library
        if (!enabled) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoadingGpsCalculation = true) }
                // prefs.isManualMode = false // Already set above
                
                try {
                    // Force GPS calculation for today (bypass Firebase)
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                    val todayTimes = prayerRepository.calculateGpsPrayerTimes(today)
                    
                    // Populate UI with GPS-calculated times
                    _uiState.update {
                        it.copy(
                            isManualMode = false,
                            fajrStart = todayTimes.fajr.start,
                            fajrJamat = todayTimes.fajr.jamaat,
                            dhuhrStart = todayTimes.dhuhr.start,
                            dhuhrJamat = todayTimes.dhuhr.jamaat,
                            asrStart = todayTimes.asr.start,
                            asrJamat = todayTimes.asr.jamaat,
                            maghribStart = todayTimes.maghrib.start,
                            maghribJamat = todayTimes.maghrib.jamaat,
                            ishaStart = todayTimes.isha.start,
                            ishaJamat = todayTimes.isha.jamaat,
                            isLoadingGpsCalculation = false,
                            hasChanges = true  // GPS times loaded = change to save
                        )
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoadingGpsCalculation = false) }
                }
            }
        }
    }

    fun onTimeChange(prayer: String, type: String, time: String) {
        _uiState.update { state ->
            val updatedState = when (prayer) {
                "fajr" -> if (type == "start") state.copy(fajrStart = time) else state.copy(fajrJamat = time)
                "dhuhr" -> if (type == "start") state.copy(dhuhrStart = time) else state.copy(dhuhrJamat = time)
                "asr" -> if (type == "start") state.copy(asrStart = time) else state.copy(asrJamat = time)
                "maghrib" -> if (type == "start") state.copy(maghribStart = time) else state.copy(maghribJamat = time)
                "isha" -> if (type == "start") state.copy(ishaStart = time) else state.copy(ishaJamat = time)
                else -> state
            }
            updatedState.copy(hasChanges = true)
        }
    }

    fun onToggleNotif(type: String, enabled: Boolean) {
        _uiState.update { state ->
            val updatedState = when (type) {
                "adhan" -> state.copy(adhanNotif = enabled)
                "jamat" -> state.copy(jamatNotif = enabled)
                "reminder" -> state.copy(reminderNotif = enabled)
                else -> state
            }
            updatedState.copy(hasChanges = true)
        }
    }

    fun onNotesChange(notes: String) {
        _uiState.update { it.copy(adminNotes = notes, hasChanges = true) }
    }

    fun onJummahStartChange(time: String) {
        _uiState.update { it.copy(jummahStart = time, hasChanges = true) }
    }

    fun onJummahJamatChange(time: String) {
        _uiState.update { it.copy(jummahJamat = time, hasChanges = true) }
    }

    fun onPinFieldChange(field: String, value: String) {
        _uiState.update { state ->
            when (field) {
                "old" -> state.copy(oldPin = value)
                "new" -> state.copy(newPin = value)
                "confirm" -> state.copy(confirmPin = value)
                else -> state
            }
        }
    }

    fun updateAdminPin() {
        val state = _uiState.value
        
        // Validation
        if (state.oldPin != prefs.adminPin) {
            _uiState.update { it.copy(showErrorToast = true, errorMessage = "Current PIN is incorrect") }
            return
        }
        
        if (state.newPin.length != 4) {
            _uiState.update { it.copy(showErrorToast = true, errorMessage = "New PIN must be 4 digits") }
            return
        }
        
        if (state.newPin != state.confirmPin) {
            _uiState.update { it.copy(showErrorToast = true, errorMessage = "New PINs do not match") }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(pinChangeLoading = true) }
                
                // Update local pref
                prefs.adminPin = state.newPin
                
                // Sync to Firebase (reuse existing sync logic)
                prayerRepository.uploadPrayerConfig()
                
                _uiState.update { 
                    it.copy(
                        pinChangeLoading = false,
                        showSuccessToast = true,
                        oldPin = "",
                        newPin = "",
                        confirmPin = ""
                    ) 
                }
                
                kotlinx.coroutines.delay(3000)
                dismissToasts()
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        pinChangeLoading = false,
                        showErrorToast = true, 
                        errorMessage = "Failed to update PIN: ${e.message}"
                    ) 
                }
            }
        }
    }

    // ... existing code ...
    
    private fun buildChangeMessage(oldState: AdminScheduleUiState?, newState: AdminScheduleUiState): String {
        if (oldState == null) return "Prayer schedule has been updated."
        
        val changes = mutableListOf<String>()
        
        // Check Fajr changes
        if (oldState.fajrStart != newState.fajrStart || oldState.fajrJamat != newState.fajrJamat) {
            changes.add("Fajr: ${oldState.fajrStart}/${oldState.fajrJamat} → ${newState.fajrStart}/${newState.fajrJamat}")
        }
        
        // Check Dhuhr changes
        if (oldState.dhuhrStart != newState.dhuhrStart || oldState.dhuhrJamat != newState.dhuhrJamat) {
            changes.add("Dhuhr: ${oldState.dhuhrStart}/${oldState.dhuhrJamat} → ${newState.dhuhrStart}/${newState.dhuhrJamat}")
        }
        
        // Check Asr changes
        if (oldState.asrStart != newState.asrStart || oldState.asrJamat != newState.asrJamat) {
            changes.add("Asr: ${oldState.asrStart}/${oldState.asrJamat} → ${newState.asrStart}/${newState.asrJamat}")
        }
        
        // Check Maghrib changes
        if (oldState.maghribStart != newState.maghribStart || oldState.maghribJamat != newState.maghribJamat) {
            changes.add("Maghrib: ${oldState.maghribStart}/${oldState.maghribJamat} → ${newState.maghribStart}/${newState.maghribJamat}")
        }
        
        // Check Isha changes
        if (oldState.ishaStart != newState.ishaStart || oldState.ishaJamat != newState.ishaJamat) {
            changes.add("Isha: ${oldState.ishaStart}/${oldState.ishaJamat} → ${newState.ishaStart}/${newState.ishaJamat}")
        }
        
        return if (changes.isEmpty()) {
            "Prayer schedule settings have been updated."
        } else {
            changes.joinToString(", ")
        }
    }

    fun saveSchedule() {
        val state = _uiState.value
        
        // Validation 1: Check if any changes were made
        if (!state.hasChanges) {
            _uiState.update { it.copy(showErrorToast = true, errorMessage = "No changes made to prayer schedule. Please make changes before saving.") }
            viewModelScope.launch {
                kotlinx.coroutines.delay(3000)
                dismissToasts()
            }
            return
        }
        
        // Validation 2: Check prayer times
        if (validateTimes()) {
            viewModelScope.launch {
                try {
                    _uiState.update { it.copy(isLoading = true) }
                    
                    // ... (prefs saving) ...
                    prefs.isManualMode = state.isManualMode
                    prefs.fajrStart = state.fajrStart
                    prefs.fajrJamat = state.fajrJamat
                    prefs.dhuhrStart = state.dhuhrStart
                    prefs.dhuhrJamat = state.dhuhrJamat
                    prefs.asrStart = state.asrStart
                    prefs.asrJamat = state.asrJamat
                    prefs.maghribStart = state.maghribStart
                    prefs.maghribJamat = state.maghribJamat
                    prefs.ishaStart = state.ishaStart
                    prefs.ishaJamat = state.ishaJamat
                    prefs.adhanNotificationsEnabled = state.adhanNotif
                    prefs.jamatNotificationsEnabled = state.jamatNotif
                    prefs.reminderBeforeJamatEnabled = state.reminderNotif

                    // Trigger sync to update Room database with these names
                    // This now also uploads to Firebase
                    try {
                        prayerRepository.uploadPrayerConfig() // Explicitly upload config
                        prayerRepository.syncPrayerTimes()    // Then schedule local alarms
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Continue even if sync fails
                    }
                    
                    // Send FCM Push Notification
                    try {
                        val changeMessage = buildChangeMessage(originalState, state)
                        notificationSender.sendTopicNotification(
                            topic = "prayer_schedule",
                            title = "Prayer Schedule Updated",
                            message = changeMessage,
                            notes = state.adminNotes
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Continue even if notification fails
                    }

                    
                    // Refresh the timestamp from Firebase
                    try {
                        fetchLastUpdatedTime()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // Continue even if timestamp fetch fails
                    }
                    
                    // Update original state after successful save
                    originalState = state
                    
                    _uiState.update { it.copy(showSuccessToast = true, isLoading = false, hasChanges = false) }
                    // Auto-dismiss after 3 seconds
                    kotlinx.coroutines.delay(3000)
                    dismissToasts()
                } catch (e: Exception) {
                    // Handle any unexpected errors
                    e.printStackTrace()
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            showErrorToast = true, 
                            errorMessage = "Failed to save schedule. Please try again."
                        ) 
                    }
                    kotlinx.coroutines.delay(3000)
                    dismissToasts()
                }
            }
        } else {
            _uiState.update { it.copy(showErrorToast = true, errorMessage = "Jamat time must be after Adhan time") }
            viewModelScope.launch {
                kotlinx.coroutines.delay(3000)
                dismissToasts()
            }
        }
    }

    private fun validateTimes(): Boolean {
        val state = _uiState.value
        return isAfter(state.fajrJamat, state.fajrStart) &&
               isAfter(state.dhuhrJamat, state.dhuhrStart) &&
               isAfter(state.asrJamat, state.asrStart) &&
               isAfter(state.maghribJamat, state.maghribStart) &&
               isAfter(state.ishaJamat, state.ishaStart)
    }

    private fun isAfter(time1: String, time2: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("HH:mm", Locale.US)
            val date1 = sdf.parse(time1)
            val date2 = sdf.parse(time2)
            date1 != null && date2 != null && date1.after(date2)
        } catch (e: Exception) {
            false
        }
    }

    fun dismissToasts() {
        _uiState.update { it.copy(showSuccessToast = false, showErrorToast = false) }
    }

    fun resetSchedule() {
        loadSettings()
    }
}
