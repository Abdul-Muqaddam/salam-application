package com.webinane.salam.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_timings")
data class PrayerTiming(
    @PrimaryKey
    val gregorianDate: String = "", // Used as unique identifier (e.g., "31-01-2026")
    val day: String = "",
    val fajrStart: String = "",
    val sunrise: String = "",
    val dhuhrStart: String = "",
    val asrStart: String = "",
    val maghribStart: String = "",
    val ishaStart: String = "",
    val fajrPrayer: String = "",
    val dhuhrPrayer: String = "",
    val asrPrayer: String = "",
    val maghribPrayer: String = "",
    val ishaPrayer: String = ""
)
