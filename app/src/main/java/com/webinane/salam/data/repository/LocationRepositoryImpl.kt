package com.webinane.salam.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.webinane.salam.domain.repository.LocationRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationRepositoryImpl(context: Context) : LocationRepository {
    private val prefs: SharedPreferences = context.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_CITY = "location_city"
        private const val KEY_COUNTRY = "location_country"
        private const val DEFAULT_CITY = "New York"
        private const val DEFAULT_COUNTRY = "USA"
    }

    override fun getLocationName(): Flow<String> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == KEY_CITY || key == KEY_COUNTRY) {
                val city = sharedPreferences.getString(KEY_CITY, DEFAULT_CITY) ?: DEFAULT_CITY
                val country = sharedPreferences.getString(KEY_COUNTRY, DEFAULT_COUNTRY) ?: DEFAULT_COUNTRY
                trySend("$city, $country")
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        
        // Initial value
        val city = prefs.getString(KEY_CITY, DEFAULT_CITY) ?: DEFAULT_CITY
        val country = prefs.getString(KEY_COUNTRY, DEFAULT_COUNTRY) ?: DEFAULT_COUNTRY
        trySend("$city, $country")
        
        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    override suspend fun updateLocationName(city: String, country: String) {
        prefs.edit()
            .putString(KEY_CITY, city)
            .putString(KEY_COUNTRY, country)
            .apply()
    }
}
