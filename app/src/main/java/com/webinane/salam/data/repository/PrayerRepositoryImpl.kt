package com.webinane.salam.data.repository

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.webinane.salam.data.local.PrayerDao
import com.webinane.salam.data.mapper.toDomain
import com.webinane.salam.data.model.PrayerTiming
import com.webinane.salam.domain.model.PrayerTimes
import com.webinane.salam.domain.repository.PrayerRepository
import com.webinane.salam.scheduler.AlarmScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class PrayerRepositoryImpl(
    private val prayerDao: PrayerDao,
    private val alarmScheduler: AlarmScheduler
) : PrayerRepository {
    override fun observePrayerForDate(date: String): Flow<PrayerTimes?> {
        return prayerDao.observePrayerForDate(date).map { it?.toDomain() }
    }

    override suspend fun syncPrayerTimes() {
        Log.d("PrayerRepository", "Starting sync")
        try {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd-MMM", Locale.US)
            val monthMap = mapOf(
                "Jan" to "01_Jan", "Feb" to "02_Feb", "Mar" to "03_Mar",
                "Apr" to "04_Apr", "May" to "05_May", "Jun" to "06_Jun",
                "Jul" to "07_Jul", "Aug" to "08_Aug", "Sep" to "09_Sep",
                "Oct" to "10_Oct", "Nov" to "11_Nov", "Dec" to "12_Dec"
            )

            val database = FirebaseDatabase.getInstance()
            val timingsToSave = mutableListOf<PrayerTiming>()

            for (i in 0 until 30) {
                val date = calendar.time
                val dateStr = dateFormat.format(date)
                val parts = dateStr.split("-")
                
                if (parts.size >= 2) {
                    val monthShort = parts[1]
                    val monthKey = monthMap[monthShort]
                    val dateKey = dateStr.replace("-", "_")
                    
                    if (monthKey != null) {
                        val path = "prayer_times/$monthKey/$dateKey"
                        val snapshot = database.getReference(path).get().await()
                        val timing = snapshot.getValue(PrayerTiming::class.java)
                        
                        if (timing != null) {
                            val finalTiming = if (timing.gregorianDate.isEmpty()) {
                                timing.copy(gregorianDate = dateStr)
                            } else {
                                timing
                            }
                            timingsToSave.add(finalTiming)
                        }
                    }
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }

            if (timingsToSave.isNotEmpty()) {
                prayerDao.insertAll(timingsToSave)
                Log.d("PrayerRepository", "Saved ${timingsToSave.size} days to local database")
                
                // We map to domain for alarm scheduler
                val domainItems = timingsToSave.map { it.toDomain() }
                alarmScheduler.schedule(domainItems)
            }
        } catch (e: Exception) {
            Log.e("PrayerRepository", "Error during sync", e)
            throw e
        }
    }
}
