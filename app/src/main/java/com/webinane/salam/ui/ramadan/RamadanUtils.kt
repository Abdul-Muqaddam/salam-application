package com.webinane.salam.ui.ramadan

import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField

data class HijriDateInfo(
    val year: Int,
    val month: Int,
    val day: Int,
    val monthName: String,
    val formattedFull: String,
    val isRamadan: Boolean,
    val raw: HijrahDate?
)

fun getCurrentHijriInfo(): HijriDateInfo {
    val hijriNow = try { HijrahDate.now() } catch(e: Exception) { null }
    val year = try { hijriNow?.get(ChronoField.YEAR) ?: 1445 } catch (e: Exception) { 1445 }
    val month = try { hijriNow?.get(ChronoField.MONTH_OF_YEAR) ?: 1 } catch (e: Exception) { 1 }
    val day = try { hijriNow?.get(ChronoField.DAY_OF_MONTH) ?: 1 } catch (e: Exception) { 1 }
    val monthName = getHijriMonthName(month)
    
    return HijriDateInfo(
        year = year,
        month = month,
        day = day,
        monthName = monthName,
        formattedFull = "$day $monthName $year Hijri",
        isRamadan = month == 9,
        raw = hijriNow
    )
}

fun getHijriMonthName(month: Int): String {
    return when (month) {
        1 -> "Muharram"
        2 -> "Safar"
        3 -> "Rabi' al-Awwal"
        4 -> "Rabi' al-Thani"
        5 -> "Jumada al-Awwal"
        6 -> "Jumada al-Thani"
        7 -> "Rajab"
        8 -> "Sha'ban"
        9 -> "Ramadan"
        10 -> "Shawwal"
        11 -> "Dhu al-Qi'dah"
        12 -> "Dhu al-Hijjah"
        else -> ""
    }
}

fun Int.toOrdinal(): String {
    if (this % 100 in 11..13) return "${this}th"
    return when (this % 10) {
        1 -> "${this}st"
        2 -> "${this}nd"
        3 -> "${this}rd"
        else -> "${this}th"
    }
}

/**
 * Check if a specific date is within the month of Ramadan
 */
fun isRamadanDate(date: java.time.LocalDate): Boolean {
    return try {
        val hijrahDate = java.time.chrono.HijrahDate.from(date)
        hijrahDate.get(java.time.temporal.ChronoField.MONTH_OF_YEAR) == 9
    } catch (e: Exception) {
        false
    }
}
