package com.webinane.salam.domain.repository.quran

import com.webinane.salam.data.remote.model.quran.SurahDto
import com.webinane.salam.data.remote.model.quran.SurahResponseAndTranslation
import kotlinx.coroutines.flow.Flow

interface QuranRepository {
    suspend fun getSurahs(): Flow<List<SurahDto>>
    suspend fun getSurahDetails(number: Int): Flow<List<SurahResponseAndTranslation>>
}
