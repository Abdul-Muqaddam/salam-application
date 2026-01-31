package com.webinane.salam.data.local

import android.content.Context
import android.content.SharedPreferences

class NotificationPreference(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)

    fun saveSoundOption(option: String) {
        prefs.edit().putString(KEY_SOUND_OPTION, option).apply()
    }

    fun getSoundOption(): String {
        return prefs.getString(KEY_SOUND_OPTION, OPTION_BEEP) ?: OPTION_BEEP
    }

    companion object {
        private const val KEY_SOUND_OPTION = "sound_option"
        const val OPTION_SILENT = "Silent"
        const val OPTION_BEEP = "Beep"
        const val OPTION_ADHAN = "Adhan"
    }
}
