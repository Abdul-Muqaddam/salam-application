package com.webinane.salam.data.mapper

import com.webinane.salam.data.model.PrayerTiming
import com.webinane.salam.domain.model.PrayerTimes
import com.webinane.salam.domain.model.PrayerTimesDetail

fun PrayerTiming.toDomain(): PrayerTimes {
    return PrayerTimes(
        date = gregorianDate,
        day = day,
        fajr = PrayerTimesDetail(fajrStart, fajrPrayer),
        dhuhr = PrayerTimesDetail(dhuhrStart, dhuhrPrayer),
        asr = PrayerTimesDetail(asrStart, asrPrayer),
        maghrib = PrayerTimesDetail(maghribStart, maghribPrayer),
        isha = PrayerTimesDetail(ishaStart, ishaPrayer),
        sunrise = sunrise
    )
}
