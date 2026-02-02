package com.webinane.salam.scheduler


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.webinane.salam.domain.model.PrayerTimes
import com.webinane.salam.receiver.PrayerNotificationReceiver
import java.util.Calendar
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

interface AlarmScheduler {
    fun schedule(item: PrayerTimes, targetDateInMillis: Long = System.currentTimeMillis())
    fun schedule(items: List<PrayerTimes>)
    fun cancelAll()
}

class AndroidAlarmScheduler(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private var minFutureTime = Long.MAX_VALUE

    override fun schedule(items: List<PrayerTimes>) {
        Log.d("AlarmScheduler", "=== STARTING FULL SCHEDULE (${items.size} days) ===")
        minFutureTime = Long.MAX_VALUE
        items.forEach { timing ->
            // Extract the date from date string
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            try {
                val date = dateFormat.parse(timing.date)
                if (date != null) {
                    val cal = Calendar.getInstance()
                    cal.time = date
                    // Use a larger spacing (e.g., 50) to avoid collisions between days
                    val baseRequestCode = (cal.get(Calendar.YEAR) % 100 * 100000) + (cal.get(Calendar.MONTH) * 100 + cal.get(Calendar.DAY_OF_MONTH)) * 50
                    scheduleWithBaseCode(timing, cal.timeInMillis, baseRequestCode)
                }
            } catch (e: Exception) {
                Log.e("AlarmScheduler", "Error parsing date for bulk schedule: ${timing.date}")
            }
        }
    }

    override fun schedule(item: PrayerTimes, targetDateInMillis: Long) {
         scheduleWithBaseCode(item, targetDateInMillis, 0)
    }

    private fun scheduleWithBaseCode(item: PrayerTimes, targetDateInMillis: Long, baseRequestCode: Int) {
        // Reset min timer if it's the first call in a batch (or just keep it updated)
        // Parse target date to get Year, Month, Day
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = targetDateInMillis
        
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) // 0-indexed
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val timings = mapOf(
            "Fajr" to Pair(item.fajr.start, item.fajr.jamaat),
            "Dhuhr" to Pair(item.dhuhr.start, item.dhuhr.jamaat),
            "Asr" to Pair(item.asr.start, item.asr.jamaat),
            "Maghrib" to Pair(item.maghrib.start, item.maghrib.jamaat),
            "Isha" to Pair(item.isha.start, item.isha.jamaat)
        )

        var offset = 0

        timings.forEach { (prayerName, times) ->
            scheduleAlarm(year, month, day, times.first, prayerName, "Begin", baseRequestCode + offset++)
            scheduleAlarm(year, month, day, times.second, prayerName, "Jamaat", baseRequestCode + offset++)
        }

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            scheduleAlarm(year, month, day, "13:00", "Jumuah 1", "Jamaat", baseRequestCode + offset++)
            scheduleAlarm(year, month, day, "14:00", "Jumuah 2", "Jamaat", baseRequestCode + offset++)
        }
        
        startCountdownLogging()
    }

    private fun scheduleAlarm(year: Int, month: Int, day: Int, timeStr: String, prayerName: String, type: String, requestCode: Int) {
        try {
            // timeStr format is HH:mm (e.g., "05:00")
            val parts = timeStr.trim().split(":")
            if (parts.size != 2) return

            var hour = parts[0].toInt()
            val minute = parts[1].toInt()

            // Fix 12-hour format parsing (CSV lacks AM/PM for PM times)
            when (prayerName) {
                "Dhuhr" -> {
                    // Dhuhr: 11 (AM) -> 11. 12 (PM) -> 12. 1..6 (PM) -> 13..18.
                    if (hour in 1..6) {
                        hour += 12
                    }
                }
                "Asr", "Maghrib", "Isha" -> {
                    // Always PM. If parsed as < 12, add 12.
                    if (hour < 12) {
                        hour += 12
                    }
                }
                // Fajr is always AM
            }

            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val currentTime = System.currentTimeMillis()
            val isFuture = calendar.timeInMillis > currentTime
            
            Log.d("AlarmScheduler", "[DEBUG] Testing $prayerName $type: Time=${calendar.time}, Now=${Date(currentTime)}, Diff=${calendar.timeInMillis - currentTime}ms")

            if (isFuture) {
                val intent = Intent(context, PrayerNotificationReceiver::class.java).apply {
                    putExtra("PRAYER_NAME", prayerName)
                    putExtra("TYPE", type)
                }

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                         alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                         Log.d("AlarmScheduler", "SUCCESS: Scheduled $prayerName $type for ${calendar.time} (Code: $requestCode)")
                    } else {
                         Log.e("AlarmScheduler", "PERMISSION DENIED: No exact alarm permission for $prayerName")
                    }
                } else {
                     alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                     Log.d("AlarmScheduler", "SUCCESS: Scheduled $prayerName $type for ${calendar.time} (Code: $requestCode)")
                }
                
                // Track next nearest alarm
                if (calendar.timeInMillis < minFutureTime) {
                    val oldMin = minFutureTime
                    minFutureTime = calendar.timeInMillis
                    Log.d("AlarmScheduler", "MIN_UPDATE: Nearest notification is now $prayerName $type at ${calendar.time} (Old was ${if (oldMin == Long.MAX_VALUE) "None" else Date(oldMin)})")
                }
            } else {
                Log.d("AlarmScheduler", "SKIP: $prayerName $type at ${calendar.time} is in the past")
            }
        } catch (e: Exception) {
            Log.e("AlarmScheduler", "ERROR scheduling $prayerName: ${e.message}")
        }
    }
    
    // Coroutine scope for background logging
    private val scope = CoroutineScope(Dispatchers.IO)
    private var timerJob: Job? = null

    private fun startCountdownLogging() {
        // Cancel previous job if running
        timerJob?.cancel()
        
        if (minFutureTime == Long.MAX_VALUE) {
            Log.d("NextNotification", "No future notifications scheduled for today yet.")
            return
        }

        timerJob = scope.launch {
            while (isActive) {
                val diff = minFutureTime - System.currentTimeMillis()
                if (diff <= 0) {
                    Log.d("NextNotification", "Notification time reached!")
                    break
                }

                val seconds = diff / 1000
                val minutes = seconds / 60
                val hours = minutes / 60
                
                val remainingMinutes = minutes % 60
                val remainingSeconds = seconds % 60
                
                Log.d("NextNotification", "Time left for next notification: $hours hours $remainingMinutes minutes $remainingSeconds seconds")
                
                delay(1000) // Update every second
            }
        }
    }

    override fun cancelAll() {
        timerJob?.cancel()
        // Implementation to cancel alarms if needed, requires tracking request codes
    }
}