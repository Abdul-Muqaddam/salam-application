package com.webinane.salam.domain.usecase

import com.webinane.salam.domain.model.PrayerTimes
import java.util.*

class GetHighlightUseCase {
    operator fun invoke(now: Date, today: PrayerTimes?): String {
        val data = today ?: return "FAJR"
        
        fun toMinutes(time: String): Int {
            return try {
                val parts = time.split(":")
                val h = parts[0].toInt()
                val m = parts[1].toInt()
                h * 60 + m
            } catch (e: Exception) { 0 }
        }

        fun to24Hour(time: String): String {
            return try {
                val parts = time.trim().split(":")
                if (parts.size == 2) {
                    var hour = parts[0].toInt()
                    val minute = parts[1]
                    if (hour < 12) { hour += 12 }
                    "$hour:$minute"
                } else time
            } catch (e: Exception) { time }
        }

        val cal = Calendar.getInstance()
        cal.time = now
        val currentMins = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)

        data class PrayerEvent(val timeInMinutes: Int, val prayerName: String)

        val events = listOf(
            PrayerEvent(toMinutes(data.fajr.start), "FAJR"),
            PrayerEvent(toMinutes(data.fajr.jamaat), "FAJR"),
            PrayerEvent(toMinutes(to24Hour(data.dhuhr.start)), "DHUHR"),
            PrayerEvent(toMinutes(to24Hour(data.dhuhr.jamaat)), "DHUHR"),
            PrayerEvent(toMinutes(to24Hour(data.asr.start)), "ASR"),
            PrayerEvent(toMinutes(to24Hour(data.asr.jamaat)), "ASR"),
            PrayerEvent(toMinutes(to24Hour(data.maghrib.start)), "MAGHRIB"),
            PrayerEvent(toMinutes(to24Hour(data.maghrib.jamaat)), "MAGHRIB"),
            PrayerEvent(toMinutes(to24Hour(data.isha.start)), "ISHA"),
            PrayerEvent(toMinutes(to24Hour(data.isha.jamaat)), "ISHA")
        ).sortedBy { it.timeInMinutes }

        val nextEvent = events.firstOrNull { it.timeInMinutes > currentMins }

        return nextEvent?.prayerName ?: "FAJR"
    }
}
