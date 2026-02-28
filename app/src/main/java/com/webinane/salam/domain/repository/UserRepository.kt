package com.webinane.salam.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserName(): Flow<String>
    suspend fun updateUserName(name: String)
}
