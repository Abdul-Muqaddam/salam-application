package com.webinane.salam.domain.repository

import com.webinane.salam.domain.model.PrayerTimes
import kotlinx.coroutines.flow.Flow

interface PrayerRepository {
    fun observePrayerForDate(date: String): Flow<PrayerTimes?>
    fun observeAllPrayerTimings(): Flow<List<PrayerTimes>>
    suspend fun syncPrayerTimes()
    suspend fun uploadPrayerConfig()
    suspend fun calculateGpsPrayerTimes(date: String): PrayerTimes
    fun observeScheduleUpdates(): Flow<Long>
}
