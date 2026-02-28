package com.webinane.salam.data.repository

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.webinane.salam.data.local.PrayerDao
import com.webinane.salam.data.local.PrayerSchedulePreference
import com.webinane.salam.data.mapper.toDomain
import com.webinane.salam.data.model.PrayerTiming
import com.webinane.salam.domain.model.PrayerTimes
import com.webinane.salam.domain.model.PrayerTimesDetail
import com.webinane.salam.domain.repository.PrayerRepository
import com.webinane.salam.scheduler.AlarmScheduler
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PrayerRepositoryImpl(
    private val prayerDao: PrayerDao,
    private val alarmScheduler: AlarmScheduler,
    private val prayerTimeCalculator: com.webinane.salam.util.PrayerTimeCalculator,
    private val locationManager: com.webinane.salam.util.LocationManager,
    private val prayerSchedulePreference: PrayerSchedulePreference,
    private val firebaseDatabase: FirebaseDatabase
) : PrayerRepository {

    init {
        // Global listener for settings and PIN sync
        firebaseDatabase.getReference("prayer_schedule")
            .addValueEventListener(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    if (snapshot.exists()) {
                        // Sync Admin PIN
                        val remotePin = snapshot.child("adminPin").getValue(String::class.java)
                        if (remotePin != null && remotePin != prayerSchedulePreference.adminPin) {
                            prayerSchedulePreference.adminPin = remotePin
                            Log.d("PrayerRepository", "Synced Admin PIN from Firebase (Global)")
                        }
                        
                        // Sync Manual Mode
                        val remoteManualMode = snapshot.child("isManualMode").getValue(Boolean::class.java)
                        if (remoteManualMode != null && remoteManualMode != prayerSchedulePreference.isManualMode) {
                            prayerSchedulePreference.isManualMode = remoteManualMode
                            Log.d("PrayerRepository", "Synced Manual Mode from Firebase (Global)")
                        }
                    }
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    Log.e("PrayerRepository", "Global settings sync cancelled", error.toException())
                }
            })
    }

    override fun observePrayerForDate(date: String): Flow<PrayerTimes?> {
        return kotlinx.coroutines.flow.callbackFlow {
            val ref = firebaseDatabase.getReference("prayer_times/$date")
            
            val listener = object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    launch {
                        var prayerTimes: PrayerTimes? = null
                        
                        if (snapshot.exists()) {
                            // Firebase has data - use it
                            val fajrStart = snapshot.child("fajr").child("start").getValue(String::class.java) ?: "05:00"
                            val fajrJamat = snapshot.child("fajr").child("jamat").getValue(String::class.java) ?: "05:30"
                            val dhuhrStart = snapshot.child("dhuhr").child("start").getValue(String::class.java) ?: "13:00"
                            val dhuhrJamat = snapshot.child("dhuhr").child("jamat").getValue(String::class.java) ?: "13:15"
                            val asrStart = snapshot.child("asr").child("start").getValue(String::class.java) ?: "16:45"
                            val asrJamat = snapshot.child("asr").child("jamat").getValue(String::class.java) ?: "17:00"
                            val maghribStart = snapshot.child("maghrib").child("start").getValue(String::class.java) ?: "19:00"
                            val maghribJamat = snapshot.child("maghrib").child("jamat").getValue(String::class.java) ?: "19:05"
                            val ishaStart = snapshot.child("isha").child("start").getValue(String::class.java) ?: "20:30"
                            val ishaJamat = snapshot.child("isha").child("jamat").getValue(String::class.java) ?: "20:45"
                            
                            prayerTimes = PrayerTimes(
                                date = date,
                                day = try {
                                    SimpleDateFormat("EEEE", Locale.US).format(SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date) ?: Date())
                                } catch (e: Exception) { "" },
                                fajr = PrayerTimesDetail(fajrStart, fajrJamat),
                                sunrise = "06:30",
                                dhuhr = PrayerTimesDetail(dhuhrStart, dhuhrJamat),
                                asr = PrayerTimesDetail(asrStart, asrJamat),
                                maghrib = PrayerTimesDetail(maghribStart, maghribJamat),
                                isha = PrayerTimesDetail(ishaStart, ishaJamat)
                            )
                            Log.d("PrayerRepository", "Real-time update from Firebase for $date")
                        }
                        
                        if (prayerTimes == null) {
                            // Fallback to GPS
                            val locationData = locationManager.getCurrentLocation()
                            val (latitude, longitude) = if (locationData != null) {
                                Pair(locationData.latitude, locationData.longitude)
                            } else {
                                Pair(40.7128, -74.0060)
                            }
                            
                            val dateObj = try {
                                SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date) ?: Date()
                            } catch (e: Exception) { Date() }
                            
                            prayerTimes = prayerTimeCalculator.calculatePrayerTimes(
                                latitude = latitude,
                                longitude = longitude,
                                date = dateObj
                            )
                        }
                        
                        trySend(prayerTimes)
                    }
                }

                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    close(error.toException())
                }
            }
            
            ref.addValueEventListener(listener)
            awaitClose { ref.removeEventListener(listener) }
        }
    }

    override fun observeAllPrayerTimings(): Flow<List<PrayerTimes>> {
        return kotlinx.coroutines.flow.flow {
            val timings = mutableListOf<PrayerTimes>()
            val calendar = Calendar.getInstance()
            
            // Optimization: Fetch data ONCE for all 7 days
            var snapshot: com.google.firebase.database.DataSnapshot? = null
            try {
                val ref = firebaseDatabase.getReference("prayer_schedule")
                snapshot = ref.get().await()
            } catch (e: Exception) {
                Log.e("PrayerRepository", "Error fetching initial config", e)
            }

            // Generate for next 7 days
            for (i in 0 until 7) {
                val date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time)
                var dayTiming: PrayerTimes? = null
                
                try {
                    if (snapshot != null && snapshot.exists()) {
                        val fajrStart = snapshot.child("fajr").child("start").getValue(String::class.java) ?: "05:00"
                        val fajrJamat = snapshot.child("fajr").child("jamat").getValue(String::class.java) ?: "05:30"
                        val dhuhrStart = snapshot.child("dhuhr").child("start").getValue(String::class.java) ?: "13:00"
                        val dhuhrJamat = snapshot.child("dhuhr").child("jamat").getValue(String::class.java) ?: "13:15"
                        val asrStart = snapshot.child("asr").child("start").getValue(String::class.java) ?: "16:45"
                        val asrJamat = snapshot.child("asr").child("jamat").getValue(String::class.java) ?: "17:00"
                        val maghribStart = snapshot.child("maghrib").child("start").getValue(String::class.java) ?: "19:00"
                        val maghribJamat = snapshot.child("maghrib").child("jamat").getValue(String::class.java) ?: "19:05"
                        val ishaStart = snapshot.child("isha").child("start").getValue(String::class.java) ?: "20:30"
                        val ishaJamat = snapshot.child("isha").child("jamat").getValue(String::class.java) ?: "20:45"
                        
                        dayTiming = PrayerTimes(
                            date = date,
                            day = SimpleDateFormat("EEEE", Locale.US).format(calendar.time),
                            fajr = PrayerTimesDetail(fajrStart, fajrJamat),
                            sunrise = "06:30",
                            dhuhr = PrayerTimesDetail(dhuhrStart, dhuhrJamat),
                            asr = PrayerTimesDetail(asrStart, asrJamat),
                            maghrib = PrayerTimesDetail(maghribStart, maghribJamat),
                            isha = PrayerTimesDetail(ishaStart, ishaJamat)
                        )
                    }
                } catch (e: Exception) {
                    Log.e("PrayerRepository", "Error processing data for $date", e)
                }
                
                // If Firebase didn't provide data, fallback to GPS calculation
                if (dayTiming == null) {
                    val locationData = locationManager.getCurrentLocation()
                    val (latitude, longitude) = if (locationData != null) {
                        Pair(locationData.latitude, locationData.longitude)
                    } else {
                        Pair(40.7128, -74.0060)
                    }
                    
                    dayTiming = prayerTimeCalculator.calculatePrayerTimes(
                        latitude = latitude,
                        longitude = longitude,
                        date = calendar.time
                    )
                }
                
                timings.add(dayTiming)
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
            
            emit(timings)
        }
    }

    override suspend fun syncPrayerTimes() {
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            Log.d("PrayerRepository", "Syncing/Scheduling alarms locally")
            try {
                // Schedule alarms based on current mode
                val timingsForAlarms = mutableListOf<PrayerTimes>()
                val calendar = Calendar.getInstance()
                
                // Generate times for next 7 days for alarm scheduling
                for (i in 0 until 7) {
                    val date = calendar.time
                    val prayerTimes = if (prayerSchedulePreference.isManualMode) {
                        // Manual Mode: Use fixed times from preferences
                        PrayerTimes(
                            date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date),
                            day = SimpleDateFormat("EEEE", Locale.US).format(date),
                            fajr = PrayerTimesDetail(prayerSchedulePreference.fajrStart, prayerSchedulePreference.fajrJamat),
                            sunrise = "06:30",
                            dhuhr = PrayerTimesDetail(prayerSchedulePreference.dhuhrStart, prayerSchedulePreference.dhuhrJamat),
                            asr = PrayerTimesDetail(prayerSchedulePreference.asrStart, prayerSchedulePreference.asrJamat),
                            maghrib = PrayerTimesDetail(prayerSchedulePreference.maghribStart, prayerSchedulePreference.maghribJamat),
                            isha = PrayerTimesDetail(prayerSchedulePreference.ishaStart, prayerSchedulePreference.ishaJamat),
                            jummah = PrayerTimesDetail(prayerSchedulePreference.jummahStart, prayerSchedulePreference.jummahJamat)
                        )
                    } else {
                        // Automatic Mode: Use GPS calculations
                        val locationData = locationManager.getCurrentLocation()
                        val (latitude, longitude) = if (locationData != null) {
                            Pair(locationData.latitude, locationData.longitude)
                        } else {
                            Pair(40.7128, -74.0060)
                        }
                        
                        val calculatedMode = prayerTimeCalculator.calculatePrayerTimes(
                            latitude = latitude,
                            longitude = longitude,
                            date = date
                        )
                        // Inject user preferences for Jamat times into the calculated times
                        calculatedMode.copy(
                            fajr = calculatedMode.fajr.copy(jamaat = prayerSchedulePreference.fajrJamat),
                            dhuhr = calculatedMode.dhuhr.copy(jamaat = prayerSchedulePreference.dhuhrJamat),
                            asr = calculatedMode.asr.copy(jamaat = prayerSchedulePreference.asrJamat),
                            maghrib = calculatedMode.maghrib.copy(jamaat = prayerSchedulePreference.maghribJamat),
                            isha = calculatedMode.isha.copy(jamaat = prayerSchedulePreference.ishaJamat),
                            jummah = PrayerTimesDetail(prayerSchedulePreference.jummahStart, prayerSchedulePreference.jummahJamat)
                        )
                    }
                    timingsForAlarms.add(prayerTimes)
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                }
                
                // Schedule alarms
                alarmScheduler.schedule(timingsForAlarms)
                Log.d("PrayerRepository", "Scheduled alarms for ${timingsForAlarms.size} days")
            } catch (e: Exception) {
                Log.e("PrayerRepository", "Error during sync", e)
                throw e
            }
        }
    }

    override suspend fun uploadPrayerConfig() {
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            Log.d("PrayerRepository", "Uploading prayer config to Firebase")
            try {
                val ref = firebaseDatabase.getReference("prayer_schedule")
                
                // Upload the current settings from preferences
                val scheduleConfig = mapOf(
                    "fajr" to mapOf("start" to prayerSchedulePreference.fajrStart, "jamat" to prayerSchedulePreference.fajrJamat),
                    "dhuhr" to mapOf("start" to prayerSchedulePreference.dhuhrStart, "jamat" to prayerSchedulePreference.dhuhrJamat),
                    "asr" to mapOf("start" to prayerSchedulePreference.asrStart, "jamat" to prayerSchedulePreference.asrJamat),
                    "maghrib" to mapOf("start" to prayerSchedulePreference.maghribStart, "jamat" to prayerSchedulePreference.maghribJamat),
                    "isha" to mapOf("start" to prayerSchedulePreference.ishaStart, "jamat" to prayerSchedulePreference.ishaJamat),
                    "jummah" to mapOf("start" to prayerSchedulePreference.jummahStart, "jamat" to prayerSchedulePreference.jummahJamat),
                    "isManualMode" to prayerSchedulePreference.isManualMode,
                    "adminPin" to prayerSchedulePreference.adminPin,
                    "updatedTimestamp" to System.currentTimeMillis()
                )
                
                ref.setValue(scheduleConfig).await()
                Log.d("PrayerRepository", "Uploaded current schedule config to Firebase")
            } catch (e: Exception) {
                Log.e("PrayerRepository", "Error uploading config", e)
                throw e
            }
        }
    }
    
    override suspend fun calculateGpsPrayerTimes(date: String): PrayerTimes {
        // Force GPS calculation, bypassing Firebase
        val locationData = locationManager.getCurrentLocation()
        val (latitude, longitude) = if (locationData != null) {
            Pair(locationData.latitude, locationData.longitude)
        } else {
            Pair(40.7128, -74.0060) // Default
        }
        
        val dateObj = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date) ?: Date()
        return prayerTimeCalculator.calculatePrayerTimes(
            latitude = latitude,
            longitude = longitude,
            date = dateObj
        )
    }

    override fun observeScheduleUpdates(): Flow<Long> {
        return kotlinx.coroutines.flow.callbackFlow {
            val ref = firebaseDatabase.getReference("prayer_schedule/updatedTimestamp")
            val listener = object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val timestamp = snapshot.getValue(Long::class.java) ?: 0L
                    trySend(timestamp)
                }
                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                    close(error.toException())
                }
            }
            ref.addValueEventListener(listener)
            awaitClose { ref.removeEventListener(listener) }
        }
    }
}
