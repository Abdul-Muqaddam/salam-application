package com.webinane.salam.util

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.asStateFlow

class AudioPlayerManager(context: Context) {
    private val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()
    
    private val _playbackState = kotlinx.coroutines.flow.MutableStateFlow(PlaybackInfo())
    val playbackState = _playbackState.asStateFlow()

    private var currentUrl: String? = null

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updatePlaybackState()
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                updatePlaybackState()
            }
            
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    stop()
                } else {
                    updatePlaybackState()
                }
            }
        })
    }

    private fun updatePlaybackState() {
        _playbackState.value = PlaybackInfo(
            isPlaying = exoPlayer.isPlaying,
            currentIndex = exoPlayer.currentMediaItemIndex,
            currentId = currentUrl
        )
    }

    fun playAudio(url: String) {
        // ... (check currentUrl logic)
        if (currentUrl == url) {
             if (exoPlayer.isPlaying) {
                 exoPlayer.pause()
             } else {
                 exoPlayer.play()
             }
        } else {
            // Set new URL tracking immediately or after? 
            // Better to stop first.
            stop() 
            // stop() clears currentUrl, so we must set it again for the new track
            
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
            currentUrl = url
        }
    }
    
    fun playPlaylist(urls: List<String>) {
        stop()
        
        val mediaItems = urls.map { MediaItem.fromUri(it) }
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
        exoPlayer.play()
        currentUrl = "PLAYLIST"
    }
    
    fun pause() {
        exoPlayer.pause()
    }
    
    fun resume() {
        if (exoPlayer.playbackState == Player.STATE_IDLE || exoPlayer.playbackState == Player.STATE_ENDED) {
           // If ended, we probably want to restart or just play? 
           // If we just stopped (IDLE), prepare might be needed if items cleared?
           // But resume() is usually called when PAUSED (READY).
           // If IDLE because of stop(), we can't resume without items.
           // If IDLE because of complete?
           if (exoPlayer.currentMediaItemIndex != -1) {
                exoPlayer.prepare() // Just in case
                exoPlayer.play()
           }
        } else {
           exoPlayer.play()
        }
    }
    
    fun stop() {
        currentUrl = null // Set null first so listeners see it
        exoPlayer.stop()
        exoPlayer.clearMediaItems()
        // Listener should fire IDLE and update state with null URL
    }

    fun release() {
        exoPlayer.release()
    }
    
    fun isPlaying(url: String): Boolean {
        // This check is a bit simplistic for playlists but works for single item toggle
        return exoPlayer.isPlaying && currentUrl == url
    }
}

data class PlaybackInfo(
    val isPlaying: Boolean = false,
    val currentIndex: Int = -1,
    val currentId: String? = null
)
