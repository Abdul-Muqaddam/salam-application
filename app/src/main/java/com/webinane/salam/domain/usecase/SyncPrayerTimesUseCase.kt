package com.webinane.salam.domain.usecase

import com.webinane.salam.domain.repository.PrayerRepository

class SyncPrayerTimesUseCase(
    private val repository: PrayerRepository
) {
    suspend operator fun invoke() {
        repository.syncPrayerTimes()
    }
}
