package com.webinane.salam.ui.navigation

import kotlinx.serialization.Serializable

object Routes {
    @Serializable
    object Home

    @Serializable
    object Notifications

    @Serializable
    object PrayerTimes

    @Serializable
    object ZakatCalculator
    
    @Serializable
    object QiblaFinder

    @Serializable
    object Ramadan

    @Serializable
    object AdminSchedule

    @Serializable
    object AdminAuth

    @Serializable
    object QuranHome

    @Serializable
    data class QuranReader(val surahNumber: Int, val surahName: String)

    @Serializable
    object Dua
}
