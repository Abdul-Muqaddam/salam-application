package com.webinane.salam.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.webinane.salam.domain.usecase.SyncPrayerTimesUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PrayerSyncWorker(
    context: Context,
    params: WorkerParameters,
    private val syncPrayerTimesUseCase: SyncPrayerTimesUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("PrayerSyncWorker", "Starting sync via Use Case")
        return try {
            syncPrayerTimesUseCase()
            Result.success()
        } catch (e: Exception) {
            Log.e("PrayerSyncWorker", "Sync failed", e)
            Result.retry()
        }
    }
}
