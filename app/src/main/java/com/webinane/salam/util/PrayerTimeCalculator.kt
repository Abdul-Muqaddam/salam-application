package com.webinane.salam.util

import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import java.text.SimpleDateFormat
import java.util.*

class PrayerTimeCalculator {
    
    /**
     * Calculate prayer times for a specific location and date
     * @param latitude GPS latitude
     * @param longitude GPS longitude
     * @param date Date for which to calculate prayer times
     * @param calculationMethod Calculation method (defaults to ISNA - Islamic Society of North America)
     * @return PrayerTime data class with all prayer times
     */
    fun calculatePrayerTimes(
        latitude: Double,
        longitude: Double,
        date: Date = Date(),
        calculationMethod: CalculationMethod = CalculationMethod.NORTH_AMERICA
    ): com.webinane.salam.domain.model.PrayerTimes {
        
        val coordinates = Coordinates(latitude, longitude)
        val calendar = Calendar.getInstance().apply { time = date }
        val dateComponents = DateComponents(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1, // Calendar.MONTH is 0-indexed
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        
        val params = calculationMethod.parameters
        val prayerTimes = PrayerTimes(coordinates, dateComponents, params)
        
        val timeFormat = SimpleDateFormat("HH:mm", Locale.US)
        
        return com.webinane.salam.domain.model.PrayerTimes(
            date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date),
            day = SimpleDateFormat("EEEE", Locale.US).format(date),
            fajr = com.webinane.salam.domain.model.PrayerTimesDetail(
                start = timeFormat.format(prayerTimes.fajr),
                jamaat = addMinutes(prayerTimes.fajr, 20) // 20 minutes after Azan
            ),
            sunrise = timeFormat.format(prayerTimes.sunrise),
            dhuhr = com.webinane.salam.domain.model.PrayerTimesDetail(
                start = timeFormat.format(prayerTimes.dhuhr),
                jamaat = addMinutes(prayerTimes.dhuhr, 30)
            ),
            asr = com.webinane.salam.domain.model.PrayerTimesDetail(
                start = timeFormat.format(prayerTimes.asr),
                jamaat = addMinutes(prayerTimes.asr, 30)
            ),
            maghrib = com.webinane.salam.domain.model.PrayerTimesDetail(
                start = timeFormat.format(prayerTimes.maghrib),
                jamaat = addMinutes(prayerTimes.maghrib, 10)
            ),
            isha = com.webinane.salam.domain.model.PrayerTimesDetail(
                start = timeFormat.format(prayerTimes.isha),
                jamaat = addMinutes(prayerTimes.isha, 30)
            )
        )
    }
    
    /**
     * Add minutes to a Date and return formatted time string
     */
    private fun addMinutes(date: Date, minutes: Int): String {
        val calendar = Calendar.getInstance().apply {
            time = date
            add(Calendar.MINUTE, minutes)
        }
        return SimpleDateFormat("HH:mm", Locale.US).format(calendar.time)
    }
}
