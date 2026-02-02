package com.webinane.salam.domain.usecase

import com.webinane.salam.domain.model.PrayerTimes
import com.webinane.salam.domain.repository.PrayerRepository
import kotlinx.coroutines.flow.Flow

class GetPrayerTimesUseCase(
    private val repository: PrayerRepository
) {
    operator fun invoke(date: String): Flow<PrayerTimes?> {
        return repository.observePrayerForDate(date)
    }

    fun observeAll(): Flow<List<PrayerTimes>> {
        return repository.observeAllPrayerTimings()
    }
}
