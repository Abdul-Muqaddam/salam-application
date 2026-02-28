package com.webinane.salam.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.webinane.salam.domain.repository.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

class UserRepositoryImpl(context: Context) : UserRepository {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_USER_NAME = "user_name"
        private const val DEFAULT_NAME = "User"
    }

    override fun getUserName(): Flow<String> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == KEY_USER_NAME) {
                trySend(sharedPreferences.getString(KEY_USER_NAME, DEFAULT_NAME) ?: DEFAULT_NAME)
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        
        // Initial value
        trySend(prefs.getString(KEY_USER_NAME, DEFAULT_NAME) ?: DEFAULT_NAME)
        
        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    override suspend fun updateUserName(name: String) {
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }
}
