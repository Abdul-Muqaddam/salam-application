package com.webinane.salam.data.local

import android.content.Context
import android.content.SharedPreferences

class DuaPreference(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("dua_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_BOOKMARKED_DUA_IDS = "bookmarked_dua_ids_list"
    }

    var bookmarkedDuaIds: List<String>
        get() = prefs.getString(KEY_BOOKMARKED_DUA_IDS, "")?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
        set(value) {
            prefs.edit().putString(KEY_BOOKMARKED_DUA_IDS, value.joinToString(",")).apply()
            _bookmarkedDuaIdsFlow.value = value
        }

    private val _bookmarkedDuaIdsFlow = kotlinx.coroutines.flow.MutableStateFlow(bookmarkedDuaIds)
    val bookmarkedDuaIdsFlow: kotlinx.coroutines.flow.Flow<List<String>> = _bookmarkedDuaIdsFlow

    fun isBookmarked(duaId: String): Boolean {
        return bookmarkedDuaIds.contains(duaId)
    }

    fun toggleBookmark(duaId: String) {
        val currentIds = bookmarkedDuaIds.toMutableList()
        if (currentIds.contains(duaId)) {
            currentIds.remove(duaId)
        } else {
            currentIds.add(duaId)
        }
        bookmarkedDuaIds = currentIds
    }
}
