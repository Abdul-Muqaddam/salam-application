package com.webinane.salam.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.webinane.salam.data.local.NotificationPreference
import com.webinane.salam.data.local.PrayerDao
import com.webinane.salam.util.NotificationHelper
import com.webinane.salam.worker.PrayerSyncWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.text.SimpleDateFormat
import java.util.*

class PrayerNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prayerName = intent.getStringExtra("PRAYER_NAME") ?: "Prayer"
        val type = intent.getStringExtra("TYPE") ?: "Time"

        val preference = NotificationPreference(context)
        val soundOption = preference.getSoundOption()
        
        // Manual Koin injection
        val notificationHelper: NotificationHelper by inject(NotificationHelper::class.java)
        val prayerDao: PrayerDao by inject(PrayerDao::class.java)
        val workManager: WorkManager by inject(WorkManager::class.java)

        notificationHelper.showNotification(
            title = "Prayer Time",
            message = "It's time for $prayerName ($type)",
            soundOption = soundOption
        )

        // Self-healing: Check if we have enough data left
        CoroutineScope(Dispatchers.IO).launch {
            val dateFormat = SimpleDateFormat("dd-MMM", Locale.US)
            val todayStr = dateFormat.format(Date())
            val count = prayerDao.getRemainingDaysCount(todayStr)
            
            android.util.Log.d("PrayerSync", "Remaining days of data: $count")
            
            if (count < 5) {
                android.util.Log.w("PrayerSync", "Low data detected ($count days). Triggering sync...")
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                val oneTimeWorkRequest = OneTimeWorkRequestBuilder<PrayerSyncWorker>()
                    .setConstraints(constraints)
                    .build()

                workManager.enqueue(oneTimeWorkRequest)
            }
        }
    }
}
