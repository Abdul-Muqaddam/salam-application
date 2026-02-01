package com.webinane.salam.domain.model

data class PrayerTimes(
    val date: String,
    val day: String,
    val fajr: PrayerTimesDetail,
    val dhuhr: PrayerTimesDetail,
    val asr: PrayerTimesDetail,
    val maghrib: PrayerTimesDetail,
    val isha: PrayerTimesDetail,
    val sunrise: String
)

data class PrayerTimesDetail(
    val start: String,
    val jamaat: String
)
