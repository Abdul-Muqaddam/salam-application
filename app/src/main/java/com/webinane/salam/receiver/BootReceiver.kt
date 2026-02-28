package com.webinane.salam.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.webinane.salam.worker.PrayerSyncWorker
import org.koin.java.KoinJavaComponent.inject

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == "android.intent.action.QUICKBOOT_POWERON") {
            Log.d("BootReceiver", "Device booted. Triggering sync/reschedule...")
            
            // Trigger Sync Worker to refresh data and reschedule alarms
            val workManager: WorkManager by inject(WorkManager::class.java)
            val syncRequest = OneTimeWorkRequestBuilder<PrayerSyncWorker>().build()
            workManager.enqueue(syncRequest)
        }
    }
}
