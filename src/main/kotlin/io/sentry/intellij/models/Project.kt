package io.sentry.intellij.models

import com.google.gson.annotations.SerializedName

data class Project(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("platform") var platform: String? = null
)
