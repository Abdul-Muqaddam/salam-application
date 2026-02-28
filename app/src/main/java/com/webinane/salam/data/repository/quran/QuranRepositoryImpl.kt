package com.webinane.salam.data.repository.quran

import com.webinane.salam.data.remote.model.quran.SurahDto
import com.webinane.salam.data.remote.model.quran.SurahResponseAndTranslation
import com.webinane.salam.data.remote.service.QuranApiService
import com.webinane.salam.domain.repository.quran.QuranRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class QuranRepositoryImpl(
    private val apiService: QuranApiService
) : QuranRepository {

    override suspend fun getSurahs(): Flow<List<SurahDto>> = flow {
        try {
            val response = apiService.getSurahs()
            if (response.code == 200) {
                emit(response.data)
            } else {
                throw IOException("Failed to fetch Surahs: ${response.status}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getSurahDetails(number: Int): Flow<List<SurahResponseAndTranslation>> = flow {
        try {
            val response = apiService.getSurahDetails(number)
            if (response.code == 200) {
                emit(response.data)
            } else {
                throw IOException("Failed to fetch Surah details: ${response.status}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
