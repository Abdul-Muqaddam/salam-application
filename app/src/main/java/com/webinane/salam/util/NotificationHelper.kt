package com.webinane.salam.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.core.app.NotificationCompat
import com.webinane.salam.MainActivity
import com.webinane.salam.R
import com.webinane.salam.data.local.NotificationPreference

import android.media.AudioAttributes
import android.net.Uri

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_SILENT_ID = "prayer_channel_silent_custom" // Changed ID to force update
        const val CHANNEL_BEEP_ID = "prayer_channel_beep_custom_v2" // Updated to force new attributes
        const val CHANNEL_ADHAN_ID = "prayer_channel_adhan_v2" // Updated to force new attributes
    }

    fun showNotification(title: String, message: String, soundOption: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Determine Channel ID based on sound option
        val channelId = when (soundOption) {
            NotificationPreference.OPTION_SILENT -> CHANNEL_SILENT_ID
            NotificationPreference.OPTION_ADHAN -> CHANNEL_ADHAN_ID
            else -> CHANNEL_BEEP_ID
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create Silent Channel (High Importance for visibility, but no sound/vibration)
            val silentChannel = NotificationChannel(
                CHANNEL_SILENT_ID,
                "Prayer Times (Silent)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(null, null)
                enableVibration(false)
            }
            notificationManager.createNotificationChannel(silentChannel)

            // Create Beep Channel (Custom Sound)
            val beepUri = Uri.parse("android.resource://${context.packageName}/${R.raw.beep}")
            val beepAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM) // Changed to Alarm for higher priority
                .build()

            val beepChannel = NotificationChannel(
                CHANNEL_BEEP_ID,
                "Prayer Times (Beep)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(beepUri, beepAttributes)
            }
            notificationManager.createNotificationChannel(beepChannel)

            // Create Adhan Channel (Custom Sound)
            val adhanUri = Uri.parse("android.resource://${context.packageName}/${R.raw.azan}")
            val adhanAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM) // Changed to Alarm for higher priority
                .build()

            val adhanChannel = NotificationChannel(
                CHANNEL_ADHAN_ID,
                "Prayer Times (Adhan)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(adhanUri, adhanAttributes)
            }
            notificationManager.createNotificationChannel(adhanChannel)
        }

        val activityIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // For pre-Oreo devices, set proper sound on Builder
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
             if (soundOption == NotificationPreference.OPTION_SILENT) {
                builder.setSound(null)
            } else if (soundOption == NotificationPreference.OPTION_ADHAN) {
                 val adhanUri = Uri.parse("android.resource://${context.packageName}/${R.raw.azan}")
                 builder.setSound(adhanUri)
            } else if (soundOption == NotificationPreference.OPTION_BEEP) {
                val beepUri = Uri.parse("android.resource://${context.packageName}/${R.raw.beep}")
                builder.setSound(beepUri)
            } else {
                builder.setDefaults(NotificationCompat.DEFAULT_SOUND)
            }
        }

        val NOTIFICATION_ID = 1
        // Cancel previous notification to stop sound immediately
        notificationManager.cancel(NOTIFICATION_ID)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
        
        // Manually play sound for reliability
        playCustomSound(soundOption)
    }

    private fun playCustomSound(soundOption: String) {
        try {
            val soundResId = when (soundOption) {
                NotificationPreference.OPTION_BEEP -> R.raw.beep
                NotificationPreference.OPTION_ADHAN -> R.raw.azan
                else -> null
            }

            if (soundResId != null) {
                val mediaPlayer = MediaPlayer.create(context, soundResId)
                mediaPlayer.setOnCompletionListener { mp -> 
                    mp.release() 
                }
                mediaPlayer.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
