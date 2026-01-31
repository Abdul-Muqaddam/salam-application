package com.webinane.salam.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.webinane.salam.data.model.PrayerTiming
import com.webinane.salam.scheduler.AlarmScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PrayerTimesViewModel(
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val _prayerTiming = MutableStateFlow<PrayerTiming?>(null)
    val prayerTiming: StateFlow<PrayerTiming?> = _prayerTiming.asStateFlow()

    private val _currentTime = MutableStateFlow("")
    val currentTime: StateFlow<String> = _currentTime.asStateFlow()
    
    private val _currentDate = MutableStateFlow("")
    val currentDate: StateFlow<String> = _currentDate.asStateFlow()
    
    private val _highlightedPrayer = MutableStateFlow("FAJR")
    val highlightedPrayer: StateFlow<String> = _highlightedPrayer.asStateFlow()

    private var todayData: PrayerTiming? = null
    private var tomorrowData: PrayerTiming? = null

    init {
        fetchAllPrayerContent()
        startClock()
    }
    
    private fun startClock() {
        // Simple ticker to update time every minute
        viewModelScope.launch {
            while (true) {
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val now = Date()
                val currentTimeStr = timeFormat.format(now)
                _currentTime.value = currentTimeStr
                
                checkAndSwitchData(now) // Check if we need to switch to tomorrow

                delay(1000L * 60) // Update every minute
            }
        }
    }

    // Helper to switch data if after Isha Jamaat
    private fun checkAndSwitchData(now: Date) {
        val today = todayData
        if (today != null) {
            // Use ISHA JAMAAT time (last event of the day), not ISHA START
            val ishaJamaatTime24 = to24Hour(today.ishaPrayer)
            val ishaJamaatDate = getTodayTimeDate(ishaJamaatTime24)

            // If NOW > Today's Isha Jamaat, show Tomorrow IF available
            if (now.after(ishaJamaatDate)) {
                 if (tomorrowData != null) {
                     if (_prayerTiming.value != tomorrowData) {
                         _prayerTiming.value = tomorrowData
                         val dateFormat = SimpleDateFormat("EEE, dd MMMM", Locale.getDefault())
                         val cal = Calendar.getInstance()
                         cal.add(Calendar.DAY_OF_YEAR, 1)
                         _currentDate.value = dateFormat.format(cal.time)
                         Log.d("PrayerUpdate", "Switched to Tomorrow's Data")
                         // Schedule alarms for tomorrow
                         tomorrowData?.let { 
                             val calTarget = Calendar.getInstance()
                             calTarget.add(Calendar.DAY_OF_YEAR, 1)
                             alarmScheduler.schedule(it, calTarget.timeInMillis) 
                         }
                     }
                     // Force highlight FAJR for tomorrow
                     _highlightedPrayer.value = "FAJR"
                 } else {
                     // Fallback to today but clearly it's night
                     if (_prayerTiming.value != todayData) {
                        _prayerTiming.value = todayData
                        val dateFormat = SimpleDateFormat("EEE, dd MMMM", Locale.getDefault())
                        _currentDate.value = dateFormat.format(now)
                        todayData?.let { alarmScheduler.schedule(it) }
                     }
                     _highlightedPrayer.value = "FAJR" // Waiting for tomorrow
                 }
            } else {
                // Still Today
                if (_prayerTiming.value != todayData) {
                    _prayerTiming.value = todayData
                    val dateFormat = SimpleDateFormat("EEE, dd MMMM", Locale.getDefault())
                     _currentDate.value = dateFormat.format(now)
                    Log.d("PrayerUpdate", "Showing Today's Data")
                    todayData?.let { alarmScheduler.schedule(it) }
                }
                
                // Calculate current highlight for Today
                updateHighlightForToday(now, today)
            }
        }
    }
    
    private fun updateHighlightForToday(now: Date, data: PrayerTiming) {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTimeStr = timeFormat.format(now)
        
        fun toMinutes(time: String): Int {
            return try {
                val parts = time.split(":")
                val h = parts[0].toInt()
                val m = parts[1].toInt()
                h * 60 + m
            } catch (e: Exception) {
                0
            }
        }

        val currentMins = toMinutes(currentTimeStr)
        
        // Create a list of all prayer events (beginning and jamaat times)
        // Each event has: (timeInMinutes, prayerName)
        data class PrayerEvent(val timeInMinutes: Int, val prayerName: String)
        
        val events = listOf(
            // FAJR events
            PrayerEvent(toMinutes(data.fajrStart), "FAJR"),
            PrayerEvent(toMinutes(data.fajrPrayer), "FAJR"),
            // DHUHR events
            PrayerEvent(toMinutes(to24Hour(data.dhuhrStart)), "DHUHR"),
            PrayerEvent(toMinutes(to24Hour(data.dhuhrPrayer)), "DHUHR"),
            // ASR events
            PrayerEvent(toMinutes(to24Hour(data.asrStart)), "ASR"),
            PrayerEvent(toMinutes(to24Hour(data.asrPrayer)), "ASR"),
            // MAGHRIB events
            PrayerEvent(toMinutes(to24Hour(data.maghribStart)), "MAGHRIB"),
            PrayerEvent(toMinutes(to24Hour(data.maghribPrayer)), "MAGHRIB"),
            // ISHA events
            PrayerEvent(toMinutes(to24Hour(data.ishaStart)), "ISHA"),
            PrayerEvent(toMinutes(to24Hour(data.ishaPrayer)), "ISHA")
        ).sortedBy { it.timeInMinutes }
        
        // Find the next upcoming event
        val nextEvent = events.firstOrNull { it.timeInMinutes > currentMins }
        
        val highlight = if (nextEvent != null) {
            nextEvent.prayerName
        } else {
            // All events have passed, next is tomorrow's Fajr
            "FAJR"
        }
        
        _highlightedPrayer.value = highlight
        
        Log.d("PrayerHighlight", "Current time: $currentTimeStr ($currentMins mins), Highlighted: $highlight")
    }
    
    private fun to24Hour(time: String): String {
        return try {
            val parts = time.trim().split(":")
            if (parts.size == 2) {
                var hour = parts[0].toInt()
                val minute = parts[1]
                if (hour < 12) { // Assuming times like "7:00" are PM
                    hour += 12
                }
                "$hour:$minute"
            } else {
                time
            }
        } catch (e: Exception) {
            Log.e("PrayerTimesViewModel", "Error converting time to 24-hour format: $time", e)
            time // Return original on error
        }
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

    private fun fetchAllPrayerContent() {
        // Fetch Today
        fetchPrayerForDate(Date()) { data ->
            todayData = data
            // Initially set today and schedule alarms
           checkAndSwitchData(Date())
        }
        
        // Fetch Tomorrow
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 1)
        fetchPrayerForDate(cal.time) { data ->
            tomorrowData = data
            checkAndSwitchData(Date()) // Re-check in case we were waiting for tomorrow
        }
    }

    private fun fetchPrayerForDate(date: Date, onResult: (PrayerTiming) -> Unit) {
        val dateFormat = SimpleDateFormat("dd-MMM", Locale.US)
        val dateStr = dateFormat.format(date) // e.g., "31-Dec"
        
        val parts = dateStr.split("-")
        if (parts.size < 2) return
        
        val day = parts[0]
        val monthShort = parts[1]
        
        val monthMap = mapOf(
            "Jan" to "01_Jan", "Feb" to "02_Feb", "Mar" to "03_Mar",
            "Apr" to "04_Apr", "May" to "05_May", "Jun" to "06_Jun",
            "Jul" to "07_Jul", "Aug" to "08_Aug", "Sep" to "09_Sep",
            "Oct" to "10_Oct", "Nov" to "11_Nov", "Dec" to "12_Dec"
        )
        
        val monthKey = monthMap[monthShort] ?: return
        val dateKey = dateStr.replace("-", "_")
        
        val path = "prayer_times/$monthKey/$dateKey"
        Log.d("PrayerFetch", "Fetching from: $path")
        
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(path)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val timing = snapshot.getValue(PrayerTiming::class.java)
                if (timing != null) {
                    onResult(timing)
                    Log.d("PrayerFetch", "Data fetched for $dateStr: $timing")
                } else {
                    Log.w("PrayerFetch", "No data found for $dateStr")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PrayerFetch", "Database error for $dateStr: ${error.message}")
            }
        })
    }
}
