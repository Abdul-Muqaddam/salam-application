package com.webinane.salam.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.webinane.salam.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


data class QiblaState(
    val locationName: String = "Fetching location...",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val qiblaDirection: Double = 0.0, // Degrees
    val currentHeading: Double = 0.0, // Degrees (Azimuth)
    val distance: Double = 0.0, // km
    val provider: String = "",

    val error: String? = null,
    val permissionGranted: Boolean = false,
    val hasSensors: Boolean = true
)

class QiblaViewModel(private val context: Context) : ViewModel(), android.hardware.SensorEventListener {

    private val _state = MutableStateFlow(QiblaState())
    val state: StateFlow<QiblaState> = _state.asStateFlow()

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as android.hardware.SensorManager

    private val kaabaLat = 21.422487
    private val kaabaLng = 39.826206
    
    // Sensor data
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    init {
        startSensors()
    }

    private fun startSensors() {
        val rotationVectorSensor = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ROTATION_VECTOR)
        if (rotationVectorSensor != null) {
            sensorManager.registerListener(
                this,
                rotationVectorSensor,
                android.hardware.SensorManager.SENSOR_DELAY_UI
            )
            _state.update { it.copy(hasSensors = true) }
        } else {
            // Fallback to Accel + Mag
            val accelerometer = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER)
            val magneticField = sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_MAGNETIC_FIELD)
            
            if (accelerometer != null && magneticField != null) {
                sensorManager.registerListener(
                    this,
                    accelerometer,
                    android.hardware.SensorManager.SENSOR_DELAY_UI
                )
                sensorManager.registerListener(
                    this,
                    magneticField,
                    android.hardware.SensorManager.SENSOR_DELAY_UI
                )
                _state.update { it.copy(hasSensors = true) }
            } else {
                _state.update { it.copy(hasSensors = false) }
            }
        }
    }

    override fun onSensorChanged(event: android.hardware.SensorEvent?) {
        if (event == null) return

        if (event.sensor.type == android.hardware.Sensor.TYPE_ROTATION_VECTOR) {
            android.hardware.SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
            updateOrientationFromMatrix()
        } else if (event.sensor.type == android.hardware.Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
            updateOrientationFallback()
        } else if (event.sensor.type == android.hardware.Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
            updateOrientationFallback()
        }
    }

    private fun updateOrientationFromMatrix() {
        android.hardware.SensorManager.getOrientation(rotationMatrix, orientationAngles)
        val azimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
        val heading = (azimuth + 360) % 360
        _state.update { it.copy(currentHeading = heading.toDouble()) }
    }
    
    private fun updateOrientationFallback() {
        val success = android.hardware.SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )
        if (success) {
            android.hardware.SensorManager.getOrientation(rotationMatrix, orientationAngles)
            val azimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
            val heading = (azimuth + 360) % 360
            _state.update { it.copy(currentHeading = heading.toDouble()) }
        }
    }

    override fun onAccuracyChanged(sensor: android.hardware.Sensor?, accuracy: Int) {
        // Do processing if needed
    }


    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }

    fun updatePermissionStatus(isGranted: Boolean) {
        _state.update { it.copy(permissionGranted = isGranted) }
        if (isGranted) {
            fetchLocation()
        } else {
            _state.update { it.copy(error = "Location permission needed", locationName = "Permission Required") }
        }
    }

    @SuppressLint("MissingPermission") // Checked in UI before calling
    fun fetchLocation() {
        viewModelScope.launch {
            if (!_state.value.permissionGranted) return@launch

            try {
                _state.update { it.copy(locationName = "Detecting...", error = null) }
                
                // Try last known location first (fast)
                var location = fusedLocationClient.lastLocation.await()
                
                // If last known is null, try getting a fresh current location
                if (location == null) {
                    val priority = com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
                    val token = com.google.android.gms.tasks.CancellationTokenSource()
                    location = fusedLocationClient.getCurrentLocation(priority, token.token).await()
                }
                
                if (location != null) {
                    val lat = location.latitude
                    val lng = location.longitude
                    val qibla = calculateQibla(lat, lng)
                    val dist = calculateDistance(lat, lng)
                    val address = getLocationName(lat, lng)
                    val prov = location.provider ?: "Unknown"
                    
                    _state.update { 
                        it.copy(
                            latitude = lat,
                            longitude = lng,
                            qiblaDirection = qibla,
                            distance = dist,
                            locationName = address,
                            provider = prov,
                            error = null
                        ) 
                    }
                } else {
                    _state.update { it.copy(locationName = "Unknown", error = "Location not found. Ensure GPS is ON.") }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, locationName = "Error") }
            }
        }
    }

    private fun calculateQibla(lat: Double, lng: Double): Double {
        val latRad = Math.toRadians(lat)
        val kaabaLatRad = Math.toRadians(kaabaLat)
        val deltaLng = Math.toRadians(kaabaLng - lng)

        val y = sin(deltaLng) * cos(kaabaLatRad)
        val x = cos(latRad) * sin(kaabaLatRad) - sin(latRad) * cos(kaabaLatRad) * cos(deltaLng)
        
        var bearing = Math.toDegrees(atan2(y, x))
        return (bearing + 360) % 360
    }

    private fun calculateDistance(lat: Double, lng: Double): Double {
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(kaabaLat - lat)
        val dLng = Math.toRadians(kaabaLng - lng)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat)) * cos(Math.toRadians(kaabaLat)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        return r * c
    }

    private fun getLocationName(lat: Double, lng: Double): String {
        // Use Geocoder if available, or just coordinate string
        val geocoder = android.location.Geocoder(context, java.util.Locale.getDefault())
        return try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                 val addresses = geocoder.getFromLocation(lat, lng, 1) 
                 if (!addresses.isNullOrEmpty()) {
                     val address = addresses[0]
                     if (!address.locality.isNullOrEmpty()) "${address.locality}, ${address.countryName}"
                     else address.countryName ?: "Lat: %.2f, Lng: %.2f".format(lat, lng)
                 } else {
                     "Lat: %.2f, Lng: %.2f".format(lat, lng)
                 }
            } else {
                 val addresses = geocoder.getFromLocation(lat, lng, 1)
                 if (!addresses.isNullOrEmpty()) {
                     val address = addresses[0]
                     if (!address.locality.isNullOrEmpty()) "${address.locality}, ${address.countryName}"
                     else address.countryName ?: "Lat: %.2f, Lng: %.2f".format(lat, lng)
                 } else {
                     "Lat: %.2f, Lng: %.2f".format(lat, lng)
                 }
            }
        } catch (e: Exception) {
            "Lat: %.2f, Lng: %.2f".format(lat, lng)
        }
    }
}
