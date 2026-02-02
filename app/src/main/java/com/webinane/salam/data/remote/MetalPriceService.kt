package com.webinane.salam.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * Service to fetch real-time gold and silver prices
 * Using MetalpriceAPI.com free tier
 */
class MetalPriceService {
    
    companion object {
        private const val BASE_URL = "https://api.metalpriceapi.com/v1/latest"
        // API key is loaded from local.properties via BuildConfig
        private val API_KEY = com.webinane.salam.BuildConfig.METAL_API_KEY
        
        // Standard Nisab quantities
        const val GOLD_NISAB_GRAMS = 87.48  // 87.48 grams of gold
        const val SILVER_NISAB_GRAMS = 612.36  // 612.36 grams of silver
    }
    
    data class MetalPrices(
        val goldPricePerGram: Double,  // in PKR
        val silverPricePerGram: Double,  // in PKR
        val goldNisab: Double,  // Total PKR for 87.48g
        val silverNisab: Double,  // Total PKR for 612.36g
        val lastUpdated: Long
    )
    
    suspend fun fetchMetalPrices(): Result<MetalPrices> = withContext(Dispatchers.IO) {
        try {
            // Fetch gold and silver prices in USD per troy ounce
            // Then convert to PKR using current exchange rate
            val url = URL("$BASE_URL?api_key=$API_KEY&base=USD&currencies=XAU,XAG")
            android.util.Log.d("MetalPriceService", "Fetching from: $BASE_URL")
            
            val connection = url.openConnection() as HttpURLConnection
            
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                android.util.Log.d("MetalPriceService", "API Response: $response")
                
                val jsonObject = JSONObject(response)
                
                if (jsonObject.getBoolean("success")) {
                    val rates = jsonObject.getJSONObject("rates")
                    
                    // API returns: how many troy ounces of gold/silver = 1 USD
                    // We need: how many USD = 1 troy ounce
                    // So we take the inverse
                    val xauRate = rates.getDouble("XAU")  // troy oz of gold per USD
                    val xagRate = rates.getDouble("XAG")  // troy oz of silver per USD
                    
                    android.util.Log.d("MetalPriceService", "XAU rate: $xauRate, XAG rate: $xagRate")
                    
                    val goldUsdPerOunce = 1.0 / xauRate  // USD per troy ounce of gold
                    val silverUsdPerOunce = 1.0 / xagRate  // USD per troy ounce of silver
                    
                    android.util.Log.d("MetalPriceService", "Gold USD/oz: $goldUsdPerOunce, Silver USD/oz: $silverUsdPerOunce")
                    
                    // Convert to USD per gram (1 troy ounce = 31.1035 grams)
                    val goldUsdPerGram = goldUsdPerOunce / 31.1035
                    val silverUsdPerGram = silverUsdPerOunce / 31.1035
                    
                    // Current USD to PKR exchange rate (approximate, Feb 2026)
                    // You can make this dynamic by calling another API if needed
                    val usdToPkr = 280.0  // 1 USD ≈ 280 PKR
                    
                    // Convert to PKR per gram
                    val goldPkrPerGram = goldUsdPerGram * usdToPkr
                    val silverPkrPerGram = silverUsdPerGram * usdToPkr
                    
                    // Calculate Nisab values in PKR
                    val goldNisab = goldPkrPerGram * GOLD_NISAB_GRAMS
                    val silverNisab = silverPkrPerGram * SILVER_NISAB_GRAMS
                    
                    android.util.Log.d("MetalPriceService", "Gold Nisab: PKR $goldNisab, Silver Nisab: PKR $silverNisab")
                    
                    Result.success(
                        MetalPrices(
                            goldPricePerGram = goldPkrPerGram,
                            silverPricePerGram = silverPkrPerGram,
                            goldNisab = goldNisab,
                            silverNisab = silverNisab,
                            lastUpdated = System.currentTimeMillis()
                        )
                    )
                } else {
                    val error = jsonObject.optString("error", "Unknown error")
                    android.util.Log.e("MetalPriceService", "API error: $error")
                    Result.failure(Exception("API returned error: $error"))
                }
            } else {
                android.util.Log.e("MetalPriceService", "HTTP Error: ${connection.responseCode}")
                Result.failure(Exception("HTTP Error: ${connection.responseCode}"))
            }
        } catch (e: Exception) {
            android.util.Log.e("MetalPriceService", "Exception: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * Get fallback values (approximate current PKR values as of Feb 2026)
     */
    fun getFallbackPrices(): MetalPrices {
        return MetalPrices(
            goldPricePerGram = 45280.0,  // ≈ PKR 3,960,000 / 87.48g
            silverPricePerGram = 771.0,   // ≈ PKR 472,000 / 612.36g
            goldNisab = 3960000.0,        // PKR for 87.48g gold
            silverNisab = 472000.0,       // PKR for 612.36g silver
            lastUpdated = System.currentTimeMillis()
        )
    }
}
