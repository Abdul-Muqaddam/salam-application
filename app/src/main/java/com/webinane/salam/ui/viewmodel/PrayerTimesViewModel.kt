package com.webinane.salam.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.webinane.salam.domain.model.PrayerTimes
import com.webinane.salam.domain.usecase.GetCountdownUseCase
import com.webinane.salam.domain.usecase.GetHighlightUseCase
import com.webinane.salam.domain.usecase.GetPrayerTimesUseCase
import com.webinane.salam.scheduler.AlarmScheduler
import com.webinane.salam.worker.PrayerSyncWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class PrayerTimesViewModel(
    private val alarmScheduler: AlarmScheduler,
    private val getPrayerTimesUseCase: GetPrayerTimesUseCase,
    private val getCountdownUseCase: GetCountdownUseCase,
    private val getHighlightUseCase: GetHighlightUseCase,
    private val workManager: WorkManager
) : ViewModel() {

    private val _prayerTiming = MutableStateFlow<PrayerTimes?>(null)
    val prayerTiming: StateFlow<PrayerTimes?> = _prayerTiming.asStateFlow()

    private val _currentTime = MutableStateFlow("")
    val currentTime: StateFlow<String> = _currentTime.asStateFlow()

    private val _currentDate = MutableStateFlow("")
    val currentDate: StateFlow<String> = _currentDate.asStateFlow()

    private val _highlightedPrayer = MutableStateFlow("FAJR")
    val highlightedPrayer: StateFlow<String> = _highlightedPrayer.asStateFlow()

    private val _countdownTimer = MutableStateFlow("00:00:00")
    val countdownTimer: StateFlow<String> = _countdownTimer.asStateFlow()

    private var todayData: PrayerTimes? = null
    private var tomorrowData: PrayerTimes? = null

    init {
        observePrayerData()
        startClock()
        setupBackgroundSync()
    }

    private fun observePrayerData() {
        val dateFormat = SimpleDateFormat("dd-MMM", Locale.US)

        // Observe Today
        viewModelScope.launch {
            val todayStr = dateFormat.format(Date())
            getPrayerTimesUseCase(todayStr).collect { data ->
                if (data != null) {
                    todayData = data
                    checkAndSwitchData(Date())
                } else {
                    triggerOneTimeSync()
                }
            }
        }

        // Observe Tomorrow
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, 1)
            val tomorrowStr = dateFormat.format(cal.time)
            getPrayerTimesUseCase(tomorrowStr).collect { data ->
                if (data != null) {
                    tomorrowData = data
                    checkAndSwitchData(Date())
                }
            }
        }
    }

    private fun setupBackgroundSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<PrayerSyncWorker>(
            7, TimeUnit.DAYS
        ).setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "PrayerSyncPeriodic",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

    private fun triggerOneTimeSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<PrayerSyncWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueue(oneTimeWorkRequest)
    }

    private fun startClock() {
        viewModelScope.launch {
            while (true) {
                val now = Date()
                
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                _currentTime.value = timeFormat.format(now)

                _countdownTimer.value = getCountdownUseCase(now, todayData, tomorrowData)

                checkAndSwitchData(now)

                delay(1000L)
            }
        }
    }

    private fun checkAndSwitchData(now: Date) {
        val today = todayData
        if (today != null) {
            val ishaJamaatTime24 = to24Hour(today.isha.jamaat)
            val ishaJamaatDate = getTodayTimeDate(ishaJamaatTime24)

            if (now.after(ishaJamaatDate)) {
                if (tomorrowData != null) {
                    if (_prayerTiming.value != tomorrowData) {
                        _prayerTiming.value = tomorrowData
                        val dateFormat = SimpleDateFormat("EEE, dd MMMM", Locale.getDefault())
                        val cal = Calendar.getInstance()
                        cal.add(Calendar.DAY_OF_YEAR, 1)
                        _currentDate.value = dateFormat.format(cal.time)
                        
                        // Note: alarmScheduler still works with todayData which is PrayerTiming
                        // We might need to update it or its mapper. 
                        // For now I'll stick to logic refactoring.
                    }
                    _highlightedPrayer.value = "FAJR"
                } else {
                    if (_prayerTiming.value != todayData) {
                        _prayerTiming.value = todayData
                        val dateFormat = SimpleDateFormat("EEE, dd MMMM", Locale.getDefault())
                        _currentDate.value = dateFormat.format(now)
                    }
                    _highlightedPrayer.value = "FAJR"
                }
            } else {
                if (_prayerTiming.value != todayData) {
                    _prayerTiming.value = todayData
                    val dateFormat = SimpleDateFormat("EEE, dd MMMM", Locale.getDefault())
                    _currentDate.value = dateFormat.format(now)
                }

                _highlightedPrayer.value = getHighlightUseCase(now, today)
            }
        }
    }

    private fun to24Hour(time: String): String {
        return try {
            val parts = time.trim().split(":")
            if (parts.size == 2) {
                var hour = parts[0].toInt()
                val minute = parts[1]
                if (hour < 12) {
                    hour += 12
                }
                "$hour:$minute"
            } else time
        } catch (e: Exception) { time }
    }

    private fun getTodayTimeDate(timeStr: String): Date {
        val cal = Calendar.getInstance()
        val parts = timeStr.split(":")
        cal.set(Calendar.HOUR_OF_DAY, parts[0].toInt())
        cal.set(Calendar.MINUTE, parts[1].toInt())
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }
}
