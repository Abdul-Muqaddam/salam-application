package com.webinane.salam.ui.dua

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webinane.salam.domain.model.dua.Dua
import com.webinane.salam.domain.model.dua.DuaCategory
import com.webinane.salam.domain.repository.DuaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DuaViewModel(
    private val duaRepository: DuaRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<DuaCategory>>(emptyList())
    val categories: StateFlow<List<DuaCategory>> = _categories.asStateFlow()

    private val _duas = MutableStateFlow<List<Dua>>(emptyList())
    val duas: StateFlow<List<Dua>> = _duas.asStateFlow()

    private val _bookmarkedDua = MutableStateFlow<Dua?>(null)
    val bookmarkedDua: StateFlow<Dua?> = _bookmarkedDua.asStateFlow()

    private val _bookmarkedDuaIds = MutableStateFlow<List<String>>(emptyList())
    val bookmarkedDuaIds: StateFlow<List<String>> = _bookmarkedDuaIds.asStateFlow()

    private val _bookmarkedDuas = MutableStateFlow<List<Dua>>(emptyList())
    val bookmarkedDuas: StateFlow<List<Dua>> = _bookmarkedDuas.asStateFlow()

    private val _selectedCategoryName = MutableStateFlow("Morning & Evening")
    val selectedCategoryName: StateFlow<String> = _selectedCategoryName.asStateFlow()

    private val _allDuas = MutableStateFlow<List<Dua>>(emptyList())
    val allDuas: StateFlow<List<Dua>> = _allDuas.asStateFlow()

    init {
        loadCategories()
        loadAllDuas()
        loadMorningEveningDuas()
        observeBookmarkedDua()
        observeBookmarkedDuaIds()
        observeAllBookmarkedDuas()
    }

    private fun loadAllDuas() {
        duaRepository.getDuas()
            .onEach { _allDuas.value = it }
            .launchIn(viewModelScope)
    }

    private fun observeAllBookmarkedDuas() {
        combine(bookmarkedDuaIds, allDuas) { ids, masterDuas ->
            ids.mapNotNull { id -> masterDuas.find { it.id == id } }
        }.onEach { _bookmarkedDuas.value = it }
            .launchIn(viewModelScope)
    }

    private fun observeBookmarkedDuaIds() {
        duaRepository.getBookmarkedDuaIds()
            .onEach { _bookmarkedDuaIds.value = it }
            .launchIn(viewModelScope)
    }

    private fun observeBookmarkedDua() {
        duaRepository.getBookmarkedDua()
            .onEach { _bookmarkedDua.value = it }
            .launchIn(viewModelScope)
    }

    fun toggleBookmark(duaId: String) {
        viewModelScope.launch {
            duaRepository.toggleBookmark(duaId)
        }
    }

    private fun loadCategories() {
        duaRepository.getCategories()
            .onEach { _categories.value = it }
            .launchIn(viewModelScope)
    }

    fun loadMorningEveningDuas() {
        _selectedCategoryName.value = "Morning & Evening"
        duaRepository.getMorningEveningDuas()
            .onEach { _duas.value = it }
            .launchIn(viewModelScope)
    }

    fun loadDuasByCategory(categoryId: String) {
        // Find category name from the list
        val categoryName = _categories.value.find { it.id == categoryId }?.name ?: "Duas"
        _selectedCategoryName.value = categoryName
        
        duaRepository.getDuasByCategory(categoryId)
            .onEach { _duas.value = it }
            .launchIn(viewModelScope)
    }
}
