package com.webinane.salam.data.model

data class PrayerTiming(
    val day: String = "",
    val gregorianDate: String = "",
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
