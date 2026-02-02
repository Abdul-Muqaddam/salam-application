package com.webinane.salam.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.chrono.HijrahChronology
import java.time.chrono.HijrahDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class CalendarDay(
    val dayOfMonth: Int,
    val hijriDay: Int,
    val hijriMonthName: String,
    val isCurrentMonth: Boolean,
    val isToday: Boolean = false
)

data class CalendarState(
    val displayHijriMonth: String,
    val currentGregorianMonth: String,
    val days: List<CalendarDay>,
    val hijriYear: Int
)

class IslamicCalendarViewModel : ViewModel() {
    private val _state = MutableStateFlow(calculateState(LocalDate.now()))
    val state: StateFlow<CalendarState> = _state.asStateFlow()

    private var currentMonthOffset = 0

    fun nextMonth() {
        currentMonthOffset++
        updateState()
    }

    fun previousMonth() {
        currentMonthOffset--
        updateState()
    }

    private fun updateState() {
        val targetDate = LocalDate.now().plusMonths(currentMonthOffset.toLong())
        _state.value = calculateState(targetDate)
    }

    private fun calculateState(date: LocalDate): CalendarState {
        val firstDayOfMonth = date.withDayOfMonth(1)
        val lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth())
        
        val days = mutableListOf<CalendarDay>()
        
        // Add padding for start of month
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 0 for Sunday
        for (i in firstDayOfWeek - 1 downTo 0) {
            val d = firstDayOfMonth.minusDays((i + 1).toLong())
            val h = HijrahDate.from(d)
            days.add(CalendarDay(
                d.dayOfMonth, 
                h.get(java.time.temporal.ChronoField.DAY_OF_MONTH), 
                getHijriMonthName(h.get(java.time.temporal.ChronoField.MONTH_OF_YEAR)),
                false
            ))
        }

        val today = LocalDate.now()
        for (i in 1..date.lengthOfMonth()) {
            val d = firstDayOfMonth.plusDays((i - 1).toLong())
            val h = HijrahDate.from(d)
            days.add(CalendarDay(
                i, 
                h.get(java.time.temporal.ChronoField.DAY_OF_MONTH), 
                getHijriMonthName(h.get(java.time.temporal.ChronoField.MONTH_OF_YEAR)),
                true, 
                d == today
            ))
        }
        
        // Add padding for end of month
        val remaining = 42 - days.size
        for (i in 1..remaining) {
            val d = lastDayOfMonth.plusDays(i.toLong())
            val h = HijrahDate.from(d)
            days.add(CalendarDay(
                d.dayOfMonth, 
                h.get(java.time.temporal.ChronoField.DAY_OF_MONTH), 
                getHijriMonthName(h.get(java.time.temporal.ChronoField.MONTH_OF_YEAR)),
                false
            ))
        }

        val gregorianMonthName = firstDayOfMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
        
        // Determine display Hijri month name (could be one or two)
        val monthNames = days.filter { it.isCurrentMonth }.map { it.hijriMonthName }.distinct()
        val displayHijriMonth = if (monthNames.size > 1) {
            "${monthNames[0]} / ${monthNames[1]}"
        } else {
            monthNames.firstOrNull() ?: ""
        }
        
        val firstVisibleHijriDate = HijrahDate.from(firstDayOfMonth)
        val hijriYear = firstVisibleHijriDate.get(java.time.temporal.ChronoField.YEAR)

        return CalendarState(
            displayHijriMonth = displayHijriMonth,
            currentGregorianMonth = gregorianMonthName,
            days = days,
            hijriYear = hijriYear
        )
    }

    private fun getHijriMonthName(month: Int): String {
        return when (month) {
            1 -> "Muharram"
            2 -> "Safar"
            3 -> "Rabi' al-Awwal"
            4 -> "Rabi' al-Thani"
            5 -> "Jumada al-Awwal"
            6 -> "Jumada al-Thani"
            7 -> "Rajab"
            8 -> "Sha'ban"
            9 -> "Ramadan"
            10 -> "Shawwal"
            11 -> "Dhu al-Qi'dah"
            12 -> "Dhu al-Hijjah"
            else -> ""
        }
    }
}
