package com.webinane.salam.data.remote.model.quran

import com.google.gson.annotations.SerializedName

data class QuranApiResponse<T>(
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: T
)
