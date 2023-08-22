package io.sentry.intellij.models

import com.google.gson.annotations.SerializedName

data class Metadata(
    @SerializedName("title") var title: String? = null,
    @SerializedName("in_app_frame_mix") var inAppFrameMix: String? = null
)
