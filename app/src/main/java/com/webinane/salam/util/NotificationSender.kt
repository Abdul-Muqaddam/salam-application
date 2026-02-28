package com.webinane.salam.util

import android.util.Log
import com.webinane.salam.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class NotificationSender(
    private val tokenProvider: FCMTokenProvider
) {

    suspend fun sendTopicNotification(topic: String, title: String, message: String, notes: String? = null) = withContext(Dispatchers.IO) {
        val finalMessage = if (!notes.isNullOrBlank()) {
            "$message\n\nNote: $notes"
        } else {
            message
        }
        try {
            val serviceAccountJson = BuildConfig.FCM_SERVICE_ACCOUNT_JSON
            if (serviceAccountJson.isNullOrEmpty()) {
                Log.e("NotificationSender", "FCM_SERVICE_ACCOUNT_JSON is missing in BuildConfig")
                return@withContext
            }

            // Get Access Token
            val accessToken = tokenProvider.getAccessToken(serviceAccountJson)
            if (accessToken == null) {
                Log.e("NotificationSender", "Failed to get access token")
                return@withContext
            }
            
            // Extract project ID from JSON (simple hack to avoid parsing full JSON twice)
            val projectId = try {
                val json = JSONObject(serviceAccountJson)
                json.getString("project_id")
            } catch (e: Exception) {
                // Fallback or log error
                Log.e("NotificationSender", "Could not parse project_id", e)
                return@withContext
            }

            val url = URL("https://fcm.googleapis.com/v1/projects/$projectId/messages:send")
            val conn = url.openConnection() as HttpURLConnection
            conn.useCaches = false
            conn.doInput = true
            conn.doOutput = true
            conn.requestMethod = "POST"
            conn.setRequestProperty("Authorization", "Bearer $accessToken")
            conn.setRequestProperty("Content-Type", "application/json")

            // V1 Payload Structure
            val json = JSONObject()
            val messageObj = JSONObject()
            
            messageObj.put("topic", topic)
            
            val notification = JSONObject()
            notification.put("title", title)
            notification.put("body", finalMessage)
            messageObj.put("notification", notification)
            
            val data = JSONObject()
            data.put("title", title)
            data.put("message", finalMessage)
            messageObj.put("data", data)
            
            // Android overrides (optional, for priority)
            val android = JSONObject()
            val androidNotification = JSONObject()
            androidNotification.put("sound", "default")
            android.put("notification", androidNotification)
            android.put("priority", "HIGH")
            messageObj.put("android", android)

            json.put("message", messageObj)

            val wr = OutputStreamWriter(conn.outputStream)
            wr.write(json.toString())
            wr.flush()

            val responseCode = conn.responseCode
            Log.d("NotificationSender", "Sent FCM V1 notification. Response Code: $responseCode")
            
            if (responseCode != 200) {
                 Log.e("NotificationSender", "Error output: ${conn.errorStream?.bufferedReader()?.use { it.readText() }}")
            }

        } catch (e: Exception) {
            Log.e("NotificationSender", "Error sending notification", e)
        }
    }
}
