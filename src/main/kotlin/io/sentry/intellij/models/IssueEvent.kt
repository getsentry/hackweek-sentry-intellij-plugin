package io.sentry.intellij.models

import com.google.gson.annotations.SerializedName

data class IssueEvent(
    @SerializedName("id") val id: String? = null,
    @SerializedName("entries") val entries: List<Entry> = emptyList()
)

data class Entry(@SerializedName("data") val data: EntryData? = null)

data class EntryData(@SerializedName("values") val values: List<EntryDataValue> = emptyList())

data class EntryDataValue(
    @SerializedName("stacktrace") val stacktrace: EntryDataValueStacktrace? = null
)

data class EntryDataValueStacktrace(
    @SerializedName("frames") val frames: List<Frames> = emptyList()
)

data class Frames(
    @SerializedName("filename") var filename: String? = null,
    @SerializedName("absPath") var absPath: String? = null,
    @SerializedName("module") var module: String? = null,
    @SerializedName("package") var packageName: String? = null,
    @SerializedName("platform") var platform: String? = null,
    @SerializedName("instructionAddr") var instructionAddr: String? = null,
    @SerializedName("symbolAddr") var symbolAddr: String? = null,
    @SerializedName("function") var function: String? = null,
    @SerializedName("rawFunction") var rawFunction: String? = null,
    @SerializedName("symbol") var symbol: String? = null,
    @SerializedName("context") var context: ArrayList<String> = arrayListOf(),
    @SerializedName("lineNo") var lineNo: Int? = null,
    @SerializedName("colNo") var colNo: String? = null,
    @SerializedName("inApp") var inApp: Boolean? = null,
    @SerializedName("trust") var trust: String? = null,
    @SerializedName("errors") var errors: String? = null,
    @SerializedName("lock") var lock: String? = null,
    @SerializedName("vars") var vars: String? = null
)
