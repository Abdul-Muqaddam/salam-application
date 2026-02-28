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
import java.util.Date
import java.util.Locale
import java.text.SimpleDateFormat
import kotlinx.coroutines.*

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
        
        // Proactively cancel alarms for these days before scheduling
        // This prevents duplicates if offsets shift or logic changes
        items.forEach { timing ->
             val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
             try {
                val date = dateFormat.parse(timing.date)
                if (date != null) {
                    val cal = Calendar.getInstance()
                    cal.time = date
                    val baseRequestCode = (cal.get(Calendar.YEAR) % 100 * 100000) + (cal.get(Calendar.MONTH) * 100 + cal.get(Calendar.DAY_OF_MONTH)) * 50
                    // Cancel potential range of offsets (0..19)
                    for (i in 0..19) {
                        try {
                            val intent = Intent(context, PrayerNotificationReceiver::class.java)
                            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                            } else {
                                PendingIntent.FLAG_UPDATE_CURRENT
                            }
                            val pendingIntent = PendingIntent.getBroadcast(context, baseRequestCode + i, intent, flags)
                            alarmManager.cancel(pendingIntent)
                            pendingIntent.cancel()
                        } catch (e: Exception) {
                            // Ignore
                        }
                    }
                }
             } catch (e: Exception) { }
        }

        items.forEach { timing ->
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            try {
                val date = dateFormat.parse(timing.date)
                if (date != null) {
                    val cal = Calendar.getInstance()
                    cal.time = date
                    val baseRequestCode = (cal.get(Calendar.YEAR) % 100 * 100000) + (cal.get(Calendar.MONTH) * 100 + cal.get(Calendar.DAY_OF_MONTH)) * 50
                    scheduleWithBaseCode(timing, cal.timeInMillis, baseRequestCode)
                }
            } catch (e: Exception) {
                Log.e("AlarmScheduler", "Error parsing date: ${timing.date}")
            }
        }
    }

    override fun schedule(item: PrayerTimes, targetDateInMillis: Long) {
         scheduleWithBaseCode(item, targetDateInMillis, 0)
    }

    private fun scheduleWithBaseCode(item: PrayerTimes, targetDateInMillis: Long, baseRequestCode: Int) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = targetDateInMillis
        
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val isFriday = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY
        val isRamadan = com.webinane.salam.ui.ramadan.isRamadanDate(
            java.time.Instant.ofEpochMilli(targetDateInMillis)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate()
        )
        
        val timings = mutableMapOf(
            "Fajr" to Pair(item.fajr.start, item.fajr.jamaat)
        )

        // Conditional Dhuhr vs Jummah
        if (isFriday && item.jummah != null) {
             timings["Jummah"] = Pair(item.jummah.start, item.jummah.jamaat)
        } else {
             timings["Dhuhr"] = Pair(item.dhuhr.start, item.dhuhr.jamaat)
        }

        timings["Asr"] = Pair(item.asr.start, item.asr.jamaat)
        timings["Maghrib"] = Pair(item.maghrib.start, item.maghrib.jamaat)
        timings["Isha"] = Pair(item.isha.start, item.isha.jamaat)

        var offset = 0
        timings.forEach { (prayerName, times) ->
            scheduleAlarm(year, month, day, times.first, prayerName, "Begin", baseRequestCode + offset++)
            scheduleAlarm(year, month, day, times.second, prayerName, "Jamaat", baseRequestCode + offset++)
        }

        // Ramadan specific notifications
        if (isRamadan) {
            // Suhoor End is same as Fajr Start
            scheduleAlarm(year, month, day, item.fajr.start, "Suhoor", "End", baseRequestCode + offset++)
            
            // Suhoor Start is 1 hour before Fajr Start
            val suhoorStartTime = subtractHour(item.fajr.start)
            scheduleAlarm(year, month, day, suhoorStartTime, "Suhoor", "Start", baseRequestCode + offset++)
            
            // Iftar is same as Maghrib Start
            scheduleAlarm(year, month, day, item.maghrib.start, "Iftar", "Time", baseRequestCode + offset++)
        }
        
        startCountdownLogging()
    }

    private fun subtractHour(timeStr: String): String {
        return try {
            val parts = timeStr.split(":")
            var hour = parts[0].toInt()
            val minute = parts[1]
            hour = (hour - 1 + 24) % 24
            String.format(Locale.US, "%02d:%s", hour, minute)
        } catch (e: Exception) {
            timeStr
        }
    }

    private fun scheduleAlarm(year: Int, month: Int, day: Int, timeStr: String, prayerName: String, type: String, requestCode: Int) {
        try {
            val parts = timeStr.trim().split(":")
            if (parts.size != 2) return

            var hour = parts[0].toInt()
            val minute = parts[1].toInt()

            when (prayerName) {
                "Dhuhr", "Jummah" -> if (hour in 1..6) hour += 12
                "Asr", "Maghrib", "Isha" -> if (hour < 12) hour += 12
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

            if (calendar.timeInMillis > System.currentTimeMillis()) {
                val intent = Intent(context, PrayerNotificationReceiver::class.java).apply {
                    putExtra("PRAYER_NAME", prayerName)
                    putExtra("TYPE", type)
                }

                val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    flags
                )

                // Use setAlarmClock for maximum reliability and exactness
                // This will show an alarm icon in the status bar, which is expected behavior for high-priority alarms
                val alarmClockInfo = AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent)
                alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
                
                Log.d("AlarmScheduler", "SCHEDULED: $prayerName $type at ${calendar.time} (setAlarmClock)")

                if (calendar.timeInMillis < minFutureTime) {
                    minFutureTime = calendar.timeInMillis
                }
            } else {
                 Log.d("AlarmScheduler", "SKIP PAST: $prayerName $type at ${calendar.time}")
            }
        } catch (e: Exception) {
            Log.e("AlarmScheduler", "ERROR: ${e.message}")
        }
    }
    
    // Coroutine scope for background logging
    private val scope = CoroutineScope(Dispatchers.IO)
    private var timerJob: Job? = null

    private fun startCountdownLogging() {
        timerJob?.cancel()
        if (minFutureTime == Long.MAX_VALUE) return

        timerJob = scope.launch {
            while (isActive) {
                val diff = minFutureTime - System.currentTimeMillis()
                if (diff <= 0) break
                // Log every minute to avoid spamming
                if (diff % 60000 < 1000) Log.d("NextAlarm", "Time to next: ${diff/1000/60} mins")
                delay(60000) 
            }
        }
    }

    override fun cancelAll() {
        timerJob?.cancel()
        // Note: To cancel alarms reliably, we need the exact PendingIntents. 
        // Since we generate request codes dynamically, exact cancellation is tricky here without storing IDs.
        // However, overwriting with the same IDs works.
    }
}