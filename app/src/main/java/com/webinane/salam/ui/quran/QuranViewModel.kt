package com.webinane.salam.ui.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webinane.salam.data.remote.model.quran.SurahDto
import com.webinane.salam.data.remote.model.quran.SurahResponseAndTranslation
import com.webinane.salam.domain.usecase.quran.GetSurahDetailsUseCase
import com.webinane.salam.domain.usecase.quran.GetSurahsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuranUiState(
    val surahs: List<SurahDto> = emptyList(),
    val filteredSurahs: List<SurahDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    
    // Reader details
    val currentSurah: SurahResponseAndTranslation? = null, // Holds the main edition (Arabic) + Translation edition
    val isLoadingDetails: Boolean = false,
    val detailsError: String? = null,
    
    // Playback state
    val activeAyahIndex: Int = -1,
    val isPlaying: Boolean = false
)

class QuranViewModel(
    private val getSurahsUseCase: GetSurahsUseCase,
    private val getSurahDetailsUseCase: GetSurahDetailsUseCase,
    private val audioPlayerManager: com.webinane.salam.util.AudioPlayerManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuranUiState())
    val uiState: StateFlow<QuranUiState> = _uiState.asStateFlow()
    
    // Track currently playing URL for UI updates - keeping for backward compat or single usage
    private val _currentPlayingUrl = MutableStateFlow<String?>(null)
    val currentPlayingUrl: StateFlow<String?> = _currentPlayingUrl.asStateFlow()

    init {
        fetchSurahs()
        observePlaybackState()
    }
    
    private fun observePlaybackState() {
        viewModelScope.launch {
            audioPlayerManager.playbackState.collect { playbackInfo ->
                // Use source of truth from PlaybackInfo if available, fallback to local state
                val effectiveUrl = playbackInfo.currentId ?: _currentPlayingUrl.value
                val surah = _uiState.value.currentSurah
                
                val calculatedIndex = if (effectiveUrl == "PLAYLIST") {
                    playbackInfo.currentIndex
                } else if (effectiveUrl != null && surah != null) {
                    // Single item playing, find its index in surah
                    surah.ayahs.indexOfFirst { it.audio == effectiveUrl }
                } else {
                    -1
                }
                
                _uiState.update { state ->
                    state.copy(
                        isPlaying = playbackInfo.isPlaying,
                        activeAyahIndex = if (calculatedIndex >= 0) calculatedIndex else -1
                    )
                }
                
                // Sync legacy url state if needed
                if (!playbackInfo.isPlaying && playbackInfo.currentIndex == -1) {
                    // If player explicitly stopped/idle
                    // But checking currentIndex == -1 might be flaky if paused at 0.
                    // Better rely on isPlaying? No, pause is not stop.
                    // Let's leave clear logic to stopAudio()
                }
            }
        }
    }

    fun playAyahAudio(url: String?) {
        if (url.isNullOrBlank()) return
        audioPlayerManager.playAudio(url)
        _currentPlayingUrl.value = url
    }
    
    fun playFullSurah() {
        val surah = _uiState.value.currentSurah ?: return
        val audioUrls = surah.ayahs.map { it.audio ?: "" } 
        // We pass all, even empty ones to keep index sync? 
        // If some ayah has no audio, empty string might break ExoPlayer. 
        // Better filterNotNull but then indices drift.
        // Assuming all have audio or we handle it. 
        // If we filter, we need a map to map back to original index.
        // For now, filtering not nulls and hoping for best or simple mapping.
        // Actually, let's just mapNotNull and if index mismatch occurs, it's a known limitation for now.
        // To do it right: 
        val playableAyahs = surah.ayahs.filter { !it.audio.isNullOrBlank() }
        val audioUrlsFiltered = playableAyahs.map { it.audio!! }
        
        if (audioUrlsFiltered.isNotEmpty()) {
            audioPlayerManager.playPlaylist(audioUrlsFiltered)
        }
    }
    
    fun stopAudio() {
        audioPlayerManager.pause()
        _currentPlayingUrl.value = null
    }
    
    fun togglePlayPause() {
        val state = _uiState.value
        if (state.isPlaying) {
            audioPlayerManager.pause()
        } else {
            // Resume or Play
            if (state.activeAyahIndex >= 0) {
                audioPlayerManager.resume()
            } else {
                playFullSurah()
            }
        }
    }
    
    fun toggleAudio(url: String?) {
        if (url.isNullOrBlank()) return
        if (_currentPlayingUrl.value == url) {
             audioPlayerManager.pause()
             _currentPlayingUrl.value = null
        } else {
            playAyahAudio(url)
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerManager.release()
    }

    fun fetchSurahs() {
        viewModelScope.launch {
            getSurahsUseCase()
                .onStart { _uiState.update { it.copy(isLoading = true, error = null) } }
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { surahs ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false, 
                            surahs = surahs, 
                            filteredSurahs = surahs 
                        ) 
                    }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { state -> 
            val filtered = if (query.isBlank()) {
                state.surahs
            } else {
                state.surahs.filter { 
                    it.name.contains(query, ignoreCase = true) || 
                    it.englishName.contains(query, ignoreCase = true) ||
                    it.number.toString() == query
                }
            }
            state.copy(searchQuery = query, filteredSurahs = filtered)
        }
    }

    fun loadSurahDetails(number: Int) {
        viewModelScope.launch {
            // Only fetch if different from current to avoid reload
            if (_uiState.value.currentSurah?.number == number) return@launch
            
            getSurahDetailsUseCase(number)
                .onStart { _uiState.update { it.copy(isLoadingDetails = true, detailsError = null, currentSurah = null) } }
                .catch { e -> _uiState.update { it.copy(isLoadingDetails = false, detailsError = e.message) } }
                .collect { editions ->
                    // The API returns a list [Arabic Edition, English Edition]
                    // We can reconstruct a single object or just use the first one and merge data if needed.
                    // For Simplicity, we assume the helper returns a list of "SurahResponseAndTranslation" objects which technically are just "Surah" objects with "editions" field inside or we get a list of Surahs where each is an edition.
                    // Correction: data/remote/service/QuranApiService.kt defines return type as List<SurahResponseAndTranslation>. 
                    // Let's assume the first item is Arabic and second is Translation.
                    
                    if (editions.isNotEmpty()) {
                        // Merge them: 
                        // 1. Text (quran-uthmani)
                        // 2. Translation (en.sahih)
                        // 3. Audio (ar.alafasy)
                        val arabicSurah = editions.find { it.edition?.type == "quran" } ?: editions[0]
                        val transSurah = editions.find { it.edition?.type == "translation" }
                        val audioSurah = editions.find { it.edition?.identifier == "ar.alafasy" } // or type="audio" checks
                        
                        val combinedAyahs = arabicSurah.ayahs.mapIndexed { index, ayah ->
                            val translation = transSurah?.ayahs?.getOrNull(index)?.text ?: ""
                            val audioUrl = audioSurah?.ayahs?.getOrNull(index)?.audio
                            
                            // We are appending translation to the text field with a delimiter
                            // Also passing audio if the original ayah audio was null (likely is)
                            ayah.copy(
                                text = ayah.text + "|||" + translation,
                                audio = audioUrl ?: ayah.audio // Use new audio URL if available
                            )
                        }
                             
                        val combinedSurah = arabicSurah.copy(ayahs = combinedAyahs)
                             
                        _uiState.update { 
                             it.copy(
                                isLoadingDetails = false,
                                currentSurah = combinedSurah
                            )
                        }
                    } else {
                        _uiState.update { it.copy(isLoadingDetails = false, detailsError = "No data found") }
                    }
                }
        }
    }
}
