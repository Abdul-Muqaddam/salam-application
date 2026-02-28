package com.webinane.salam.domain.usecase.quran

import com.webinane.salam.data.remote.model.quran.SurahDto
import com.webinane.salam.domain.repository.quran.QuranRepository
import kotlinx.coroutines.flow.Flow

class GetSurahsUseCase(private val repository: QuranRepository) {
    suspend operator fun invoke(): Flow<List<SurahDto>> {
        return repository.getSurahs()
    }
}
