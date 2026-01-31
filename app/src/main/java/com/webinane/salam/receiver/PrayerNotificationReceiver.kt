package com.webinane.salam.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.webinane.salam.data.local.NotificationPreference
import com.webinane.salam.util.NotificationHelper
import org.koin.java.KoinJavaComponent.inject

class PrayerNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prayerName = intent.getStringExtra("PRAYER_NAME") ?: "Prayer"
        val type = intent.getStringExtra("TYPE") ?: "Time" // "Begin" or "Jamaat"

        val preference = NotificationPreference(context)
        val soundOption = preference.getSoundOption()
        android.util.Log.d("NotificationDebug", "Receiver triggered for $prayerName. Sound Option: $soundOption")
        
        // Manual Koin injection for BroadcastReceiver
        val notificationHelper: NotificationHelper by inject(NotificationHelper::class.java)

        notificationHelper.showNotification(
            title = "Prayer Time",
            message = "It's time for $prayerName ($type)",
            soundOption = soundOption
        )
    }
}
