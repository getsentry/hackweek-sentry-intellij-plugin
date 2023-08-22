package io.sentry.intellij.models

import com.google.gson.annotations.SerializedName

data class Issue(
    @SerializedName("id") var id: String? = null,
    @SerializedName("shareId") var shareId: String? = null,
    @SerializedName("shortId") var shortId: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("culprit") var culprit: String? = null,
    @SerializedName("permalink") var permalink: String? = null,
    @SerializedName("logger") var logger: String? = null,
    @SerializedName("level") var level: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("substatus") var substatus: String? = null,
    @SerializedName("isPublic") var isPublic: Boolean? = null,
    @SerializedName("platform") var platform: String? = null,
    @SerializedName("project") var project: Project? = Project(),
    @SerializedName("type") var type: String? = null,
    @SerializedName("metadata") var metadata: Metadata? = Metadata(),
    @SerializedName("numComments") var numComments: Int? = null,
    @SerializedName("assignedTo") var assignedTo: String? = null,
    @SerializedName("isBookmarked") var isBookmarked: Boolean? = null,
    @SerializedName("isSubscribed") var isSubscribed: Boolean? = null,
    @SerializedName("subscriptionDetails") var subscriptionDetails: String? = null,
    @SerializedName("hasSeen") var hasSeen: Boolean? = null,
    @SerializedName("annotations") var annotations: ArrayList<String> = arrayListOf(),
    @SerializedName("issueType") var issueType: String? = null,
    @SerializedName("issueCategory") var issueCategory: String? = null,
    @SerializedName("isUnhandled") var isUnhandled: Boolean? = null,
    @SerializedName("count") var count: String? = null,
    @SerializedName("userCount") var userCount: Int? = null,
    @SerializedName("firstSeen") var firstSeen: String? = null,
    @SerializedName("lastSeen") var lastSeen: String? = null,
    @SerializedName("stats") var stats: Stats? = Stats()
)
