package com.webinane.salam.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Try different Arabic locales for better compatibility
            val locales = listOf(
                Locale("ar", "SA"),
                Locale("ar"),
                Locale("ara")
            )
            
            var supported = false
            for (locale in locales) {
                val result = tts?.setLanguage(locale)
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    supported = true
                    Log.d("TtsManager", "Set language to ${locale.displayName}")
                    break
                }
            }

            if (!supported) {
                Log.e("TtsManager", "Arabic language is not supported or missing data")
                tts?.language = Locale.US
            }
            isInitialized = true
        } else {
            Log.e("TtsManager", "Initialization failed")
        }
    }

    fun speak(text: String): Boolean {
        if (isInitialized) {
            val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "DuaSpeech")
            return result == TextToSpeech.SUCCESS
        }
        return false
    }

    fun isReady() = isInitialized

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
