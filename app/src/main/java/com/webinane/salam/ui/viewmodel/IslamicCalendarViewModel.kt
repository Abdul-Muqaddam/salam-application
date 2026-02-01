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
    val isCurrentMonth: Boolean,
    val isToday: Boolean = false
)

data class CalendarState(
    val currentHijriMonth: String,
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
        
        val hijrahDate = HijrahDate.from(firstDayOfMonth)
        val hijriMonthName = getHijriMonthName(hijrahDate.get(java.time.temporal.ChronoField.MONTH_OF_YEAR))
        val hijriYear = hijrahDate.get(java.time.temporal.ChronoField.YEAR)
        
        val gregorianMonthName = firstDayOfMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()))
        
        val days = mutableListOf<CalendarDay>()
        
        // Add padding for start of month
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 0 for Sunday
        val prevMonth = firstDayOfMonth.minusMonths(1)
        val prevMonthLen = prevMonth.lengthOfMonth()
        for (i in firstDayOfWeek - 1 downTo 0) {
            val d = firstDayOfMonth.minusDays((i + 1).toLong())
            val h = HijrahDate.from(d)
            days.add(CalendarDay(d.dayOfMonth, h.get(java.time.temporal.ChronoField.DAY_OF_MONTH), false))
        }

        val today = LocalDate.now()
        for (i in 1..date.lengthOfMonth()) {
            val d = firstDayOfMonth.plusDays((i - 1).toLong())
            val h = HijrahDate.from(d)
            days.add(CalendarDay(i, h.get(java.time.temporal.ChronoField.DAY_OF_MONTH), true, d == today))
        }
        
        // Add padding for end of month
        val remaining = 42 - days.size
        for (i in 1..remaining) {
            val d = lastDayOfMonth.plusDays(i.toLong())
            val h = HijrahDate.from(d)
            days.add(CalendarDay(d.dayOfMonth, h.get(java.time.temporal.ChronoField.DAY_OF_MONTH), false))
        }

        return CalendarState(
            currentHijriMonth = hijriMonthName,
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
