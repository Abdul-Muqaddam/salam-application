package com.webinane.salam.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.webinane.salam.data.local.DhikrPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DhikrViewModel(private val preference: DhikrPreference) : ViewModel() {
    private val _count = MutableStateFlow(preference.count)
    val count: StateFlow<Int> = _count.asStateFlow()

    private val _totalCount = MutableStateFlow(preference.totalCount)
    val totalCount: StateFlow<Int> = _totalCount.asStateFlow()

    val targetOptions = listOf(33, 99)

    fun incrementCount() {
        if (_count.value < _totalCount.value) {
            _count.value++
            preference.count = _count.value
        } else {
            _count.value = 0
            preference.count = 0
        }
    }

    fun setTotalCount(target: Int) {
        _totalCount.value = target
        preference.totalCount = target
        if (_count.value > _totalCount.value) {
            _count.value = _totalCount.value
            preference.count = _totalCount.value
        }
    }
    
    fun resetCount() {
        _count.value = 0
        preference.count = 0
    }
}
