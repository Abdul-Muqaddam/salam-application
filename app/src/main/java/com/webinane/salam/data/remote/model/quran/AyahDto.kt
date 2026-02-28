package com.webinane.salam.data.remote.model.quran

import com.google.gson.annotations.SerializedName

data class AyahDto(
    @SerializedName("number") val number: Int,
    @SerializedName("text") val text: String,
    @SerializedName("numberInSurah") val numberInSurah: Int,
    @SerializedName("juz") val juz: Int,
    @SerializedName("manzil") val manzil: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("ruku") val ruku: Int,
    @SerializedName("hizbQuarter") val hizbQuarter: Int,
    @SerializedName("sajda") val sajda: Any?, // Can be boolean or object
    @SerializedName("audio") val audio: String? = null, // URL for audio
    @SerializedName("audioSecondary") val audioSecondary: List<String>? = null
)
