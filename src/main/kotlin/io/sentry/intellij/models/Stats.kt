package io.sentry.intellij.models

import com.google.gson.annotations.SerializedName

data class Stats(
    @SerializedName("24h") var twentyFourHr: ArrayList<ArrayList<Int>> = arrayListOf()
)
