package com.webinane.salam.data.local

import android.content.Context
import android.content.SharedPreferences

class PrayerSchedulePreference(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("prayer_schedule_prefs", Context.MODE_PRIVATE)

    var isManualMode: Boolean
        get() = prefs.getBoolean(KEY_IS_MANUAL_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_MANUAL_MODE, value).apply()

    // Fajr
    var fajrStart: String
        get() = prefs.getString(KEY_FAJR_START, "05:30") ?: "05:30"
        set(value) = prefs.edit().putString(KEY_FAJR_START, value).apply()
    
    var fajrJamat: String
        get() = prefs.getString(KEY_FAJR_JAMAT, "05:45") ?: "05:45"
        set(value) = prefs.edit().putString(KEY_FAJR_JAMAT, value).apply()

    // Dhuhr
    var dhuhrStart: String
        get() = prefs.getString(KEY_DHUHR_START, "13:15") ?: "13:15"
        set(value) = prefs.edit().putString(KEY_DHUHR_START, value).apply()
    
    var dhuhrJamat: String
        get() = prefs.getString(KEY_DHUHR_JAMAT, "13:30") ?: "13:30"
        set(value) = prefs.edit().putString(KEY_DHUHR_JAMAT, value).apply()

    // Asr
    var asrStart: String
        get() = prefs.getString(KEY_ASR_START, "16:45") ?: "16:45"
        set(value) = prefs.edit().putString(KEY_ASR_START, value).apply()
    
    var asrJamat: String
        get() = prefs.getString(KEY_ASR_JAMAT, "17:00") ?: "17:00"
        set(value) = prefs.edit().putString(KEY_ASR_JAMAT, value).apply()

    // Maghrib
    var maghribStart: String
        get() = prefs.getString(KEY_MAGHRIB_START, "19:20") ?: "19:20"
        set(value) = prefs.edit().putString(KEY_MAGHRIB_START, value).apply()
    
    var maghribJamat: String
        get() = prefs.getString(KEY_MAGHRIB_JAMAT, "19:25") ?: "19:25"
        set(value) = prefs.edit().putString(KEY_MAGHRIB_JAMAT, value).apply()

    // Isha
    var ishaStart: String
        get() = prefs.getString(KEY_ISHA_START, "20:45") ?: "20:45"
        set(value) = prefs.edit().putString(KEY_ISHA_START, value).apply()
    
    var ishaJamat: String
        get() = prefs.getString(KEY_ISHA_JAMAT, "21:00") ?: "21:00"
        set(value) = prefs.edit().putString(KEY_ISHA_JAMAT, value).apply()

    // Jummah
    var jummahStart: String
        get() = prefs.getString(KEY_JUMMAH_START, "13:00") ?: "13:00"
        set(value) = prefs.edit().putString(KEY_JUMMAH_START, value).apply()

    var jummahJamat: String
        get() = prefs.getString(KEY_JUMMAH_JAMAT, "13:30") ?: "13:30"
        set(value) = prefs.edit().putString(KEY_JUMMAH_JAMAT, value).apply()

    // Notifications
    var adhanNotificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_ADHAN_NOTIF, true)
        set(value) = prefs.edit().putBoolean(KEY_ADHAN_NOTIF, value).apply()

    var jamatNotificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_JAMAT_NOTIF, true)
        set(value) = prefs.edit().putBoolean(KEY_JAMAT_NOTIF, value).apply()

    var reminderBeforeJamatEnabled: Boolean
        get() = prefs.getBoolean(KEY_REMINDER_NOTIF, false)
        set(value) = prefs.edit().putBoolean(KEY_REMINDER_NOTIF, value).apply()

    var adminPin: String
        get() = prefs.getString(KEY_ADMIN_PIN, "2508") ?: "2508"
        set(value) = prefs.edit().putString(KEY_ADMIN_PIN, value).apply()

    companion object {
        private const val KEY_IS_MANUAL_MODE = "is_manual_mode"
        private const val KEY_FAJR_START = "fajr_start"
        private const val KEY_FAJR_JAMAT = "fajr_jamat"
        private const val KEY_DHUHR_START = "dhuhr_start"
        private const val KEY_DHUHR_JAMAT = "dhuhr_jamat"
        private const val KEY_ASR_START = "asr_start"
        private const val KEY_ASR_JAMAT = "asr_jamat"
        private const val KEY_MAGHRIB_START = "maghrib_start"
        private const val KEY_MAGHRIB_JAMAT = "maghrib_jamat"
        private const val KEY_ISHA_START = "isha_start"
        private const val KEY_ISHA_JAMAT = "isha_jamat"
        private const val KEY_JUMMAH_START = "jummah_start"
        private const val KEY_JUMMAH_JAMAT = "jummah_jamat"
        private const val KEY_ADHAN_NOTIF = "adhan_notif"
        private const val KEY_JAMAT_NOTIF = "jamat_notif"
        private const val KEY_REMINDER_NOTIF = "reminder_notif"
        private const val KEY_ADMIN_PIN = "admin_pin"
    }
}
