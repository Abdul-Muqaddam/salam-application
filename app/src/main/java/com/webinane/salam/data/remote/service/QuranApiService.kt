package com.webinane.salam.data.remote.service

import com.webinane.salam.data.remote.model.quran.QuranApiResponse
import com.webinane.salam.data.remote.model.quran.SurahDto
import com.webinane.salam.data.remote.model.quran.SurahResponseAndTranslation
import retrofit2.http.GET
import retrofit2.http.Path

interface QuranApiService {
    
    // Get list of all Surahs
    @GET("surah")
    suspend fun getSurahs(): QuranApiResponse<List<SurahDto>>

    // Get Surah details with specific editions (e.g. quran-uthmani,en.sahih,ar.alafasy)
    // The API returns a list of "Surah" objects, one for each edition requested.
    @GET("surah/{number}/editions/quran-uthmani,en.sahih,ar.alafasy")
    suspend fun getSurahDetails(@Path("number") number: Int): QuranApiResponse<List<SurahResponseAndTranslation>>
    
    // Get Surah with Audio (e.g. ar.alafasy)
    // Note: It's better to fetch audio URLs separately or construct them to save bandwidth, 
    // but for now we can rely on standard audio endpoints or constructing the URL: 
    // https://cdn.islamic.network/quran/audio/128/ar.alafasy/{ayahId}.mp3
}
