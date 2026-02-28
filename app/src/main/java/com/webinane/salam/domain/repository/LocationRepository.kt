package com.webinane.salam.domain.repository

import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationName(): Flow<String>
    suspend fun updateLocationName(city: String, country: String)
}
