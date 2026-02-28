package com.webinane.salam.util

import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream

class FCMTokenProvider {
    
    suspend fun getAccessToken(serviceAccountJson: String): String? = withContext(Dispatchers.IO) {
        try {
            val stream = ByteArrayInputStream(serviceAccountJson.toByteArray())
            val credentials = GoogleCredentials.fromStream(stream)
                .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
            
            credentials.refreshIfExpired()
            return@withContext credentials.accessToken.tokenValue
        } catch (e: Exception) {
            Log.e("FCMTokenProvider", "Error generating access token", e)
            return@withContext null
        }
    }
}
