package com.webinane.salam.domain.usecase

import com.webinane.salam.domain.model.PrayerTimes
import java.util.*

class GetCountdownUseCase {
    operator fun invoke(now: Date, today: PrayerTimes?, tomorrow: PrayerTimes?): String {
        val data = today ?: return "00:00:00"
        
        fun to24Hour(time: String): String {
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

        val cal = Calendar.getInstance()
        cal.time = now
        val currentTotalSeconds = cal.get(Calendar.HOUR_OF_DAY) * 3600 + 
                                 cal.get(Calendar.MINUTE) * 60 + 
                                 cal.get(Calendar.SECOND)

        // List of events to check
        val events = listOf(
            "Fajr" to data.fajr.start,
            "Fajr" to data.fajr.jamaat,
            "Dhuhr" to data.dhuhr.start,
            "Dhuhr" to data.dhuhr.jamaat,
            "Asr" to data.asr.start,
            "Asr" to data.asr.jamaat,
            "Maghrib" to data.maghrib.start,
            "Maghrib" to data.maghrib.jamaat,
            "Isha" to data.isha.start,
            "Isha" to data.isha.jamaat
        ).map { (name, time) -> 
            val parts = time.trim().split(":")
            var hour = parts[0].toInt()
            val minute = parts[1].toInt()
            
            when (name) {
                "Dhuhr" -> if (hour in 1..6) hour += 12
                "Asr", "Maghrib", "Isha" -> if (hour < 12) hour += 12
            }
            hour * 3600 + minute * 60
        }.sorted()

        // Find the next event
        val nextEventSeconds = events.firstOrNull { it > currentTotalSeconds }

        return if (nextEventSeconds != null) {
            val diff = nextEventSeconds - currentTotalSeconds
            val h = diff / 3600
            val m = (diff % 3600) / 60
            val s = diff % 60
            String.format("%02d:%02d:%02d", h, m, s)
        } else {
            // All passed for today, wait for tomorrow's Fajr
            tomorrow?.let { tom ->
                // Direct parsing for Fajr as it's always AM
                val parts = tom.fajr.start.trim().split(":")
                val tomorrowFajrSeconds = parts[0].toInt() * 3600 + parts[1].toInt() * 60
                
                val diff = (24 * 3600 - currentTotalSeconds) + tomorrowFajrSeconds
                val h = diff / 3600
                val m = (diff % 3600) / 60
                val s = diff % 60
                String.format("%02d:%02d:%02d", h, m, s)
            } ?: "00:00:00"
        }
    }
}
