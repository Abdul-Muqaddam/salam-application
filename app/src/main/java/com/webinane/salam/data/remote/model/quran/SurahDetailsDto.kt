package com.webinane.salam.data.remote.model.quran

import com.google.gson.annotations.SerializedName

data class SurahResponseAndTranslation(
    @SerializedName("number") val number: Int,
    @SerializedName("name") val name: String,
    @SerializedName("englishName") val englishName: String,
    @SerializedName("englishNameTranslation") val englishNameTranslation: String,
    @SerializedName("revelationType") val revelationType: String,
    @SerializedName("numberOfAyahs") val numberOfAyahs: Int,
    @SerializedName("ayahs") val ayahs: List<AyahDto>,
    @SerializedName("edition") val edition: EditionDto? = null
)

data class EditionDto(
    @SerializedName("identifier") val identifier: String,
    @SerializedName("language") val language: String,
    @SerializedName("name") val name: String,
    @SerializedName("englishName") val englishName: String,
    @SerializedName("format") val format: String,
    @SerializedName("type") val type: String
)
