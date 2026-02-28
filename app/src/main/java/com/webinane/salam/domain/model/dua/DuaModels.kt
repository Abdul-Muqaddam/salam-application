package com.webinane.salam.domain.model.dua

import androidx.compose.ui.graphics.Color

data class DuaCategory(
    val id: String,
    val name: String,
    val iconRes: Int,
    val duaCount: Int,
    val bgColor: Color,
    val iconBgColor: Color
)

data class Dua(
    val id: String,
    val title: String,
    val arabicText: String,
    val transliteration: String,
    val translation: String,
    val type: String, // e.g., "Morning", "Evening", "Protection"
    val categoryId: String,
    val audioRes: Int? = null
)
