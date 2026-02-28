package com.webinane.salam.domain.repository

import com.webinane.salam.domain.model.dua.Dua
import com.webinane.salam.domain.model.dua.DuaCategory
import kotlinx.coroutines.flow.Flow

interface DuaRepository {
    fun getCategories(): Flow<List<DuaCategory>>
    fun getDuasByCategory(categoryId: String): Flow<List<Dua>>
    fun getMorningEveningDuas(): Flow<List<Dua>>
    fun getDuas(): Flow<List<Dua>>
    fun getBookmarkedDua(): Flow<Dua?>
    fun getBookmarkedDuaIds(): Flow<List<String>>
    suspend fun toggleBookmark(duaId: String)
}
