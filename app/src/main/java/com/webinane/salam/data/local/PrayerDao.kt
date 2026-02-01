package com.webinane.salam.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.webinane.salam.data.model.PrayerTiming
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerDao {
    @Query("SELECT * FROM prayer_timings WHERE gregorianDate = :date LIMIT 1")
    suspend fun getPrayerForDate(date: String): PrayerTiming?

    @Query("SELECT * FROM prayer_timings WHERE gregorianDate = :date LIMIT 1")
    fun observePrayerForDate(date: String): Flow<PrayerTiming?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(timings: List<PrayerTiming>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timing: PrayerTiming)

    @Query("SELECT COUNT(*) FROM prayer_timings WHERE gregorianDate >= :startDate")
    suspend fun getRemainingDaysCount(startDate: String): Int
    
    @Query("SELECT * FROM prayer_timings ORDER BY gregorianDate ASC")
    fun getAllPrayerTimings(): Flow<List<PrayerTiming>>
}
