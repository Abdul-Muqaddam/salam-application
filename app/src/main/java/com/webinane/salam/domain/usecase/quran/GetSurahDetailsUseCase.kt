package com.webinane.salam.domain.usecase.quran

import com.webinane.salam.data.remote.model.quran.SurahResponseAndTranslation
import com.webinane.salam.domain.repository.quran.QuranRepository
import kotlinx.coroutines.flow.Flow

class GetSurahDetailsUseCase(private val repository: QuranRepository) {
    suspend operator fun invoke(number: Int): Flow<List<SurahResponseAndTranslation>> {
        return repository.getSurahDetails(number)
    }
}
