package com.webinane.salam.data.local

import android.content.Context
import android.content.SharedPreferences

class DhikrPreference(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("dhikr_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_COUNT = "current_count"
        private const val KEY_TOTAL = "total_count"
    }

    var count: Int
        get() = prefs.getInt(KEY_COUNT, 0)
        set(value) = prefs.edit().putInt(KEY_COUNT, value).apply()

    var totalCount: Int
        get() = prefs.getInt(KEY_TOTAL, 33)
        set(value) = prefs.edit().putInt(KEY_TOTAL, value).apply()

    fun reset() {
        prefs.edit().putInt(KEY_COUNT, 0).apply()
    }
}
