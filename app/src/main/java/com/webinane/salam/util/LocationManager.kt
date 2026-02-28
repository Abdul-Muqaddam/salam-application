package com.webinane.salam.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationManager(private val context: Context) {
    
    data class LocationData(
        val city: String,
        val country: String,
        val latitude: Double,
        val longitude: Double
    )
    
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    
    suspend fun getCurrentLocation(): LocationData? {
        if (!hasLocationPermission()) {
            Log.w("LocationManager", "Location permission not granted")
            return null
        }
        
        return try {
            val location = suspendCancellableCoroutine { continuation ->
                val cancellationToken = CancellationTokenSource()
                
                continuation.invokeOnCancellation {
                    cancellationToken.cancel()
                }
                
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    cancellationToken.token
                ).addOnSuccessListener { location ->
                    if (location != null) {
                        continuation.resume(location)
                    } else {
                        continuation.resumeWithException(Exception("Location is null"))
                    }
                }.addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
            }
            
            // Geocode to get city and country
            // Geocoder can block, so we must run it on IO dispatcher
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>? = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                try {
                    geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )
                } catch (e: Exception) {
                    Log.e("LocationManager", "Geocoder failed", e)
                    null
                }
            }
            
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val city = address.locality ?: address.subAdminArea ?: address.adminArea ?: "Unknown City"
                val country = address.countryName ?: "Unknown Country"
                Log.d("LocationManager", "Location detected: $city, $country (${location.latitude}, ${location.longitude})")
                LocationData(city, country, location.latitude, location.longitude)
            } else {
                Log.w("LocationManager", "No address found for coordinates")
                LocationData("Unknown City", "Unknown Country", location.latitude, location.longitude)
            }
        } catch (e: Exception) {
            Log.e("LocationManager", "Error getting location", e)
            null
        }
    }
    
    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
