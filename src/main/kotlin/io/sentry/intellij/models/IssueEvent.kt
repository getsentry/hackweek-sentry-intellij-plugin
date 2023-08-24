package io.sentry.intellij.models

import com.google.gson.annotations.SerializedName

data class IssueEvent(
    @SerializedName("id") val id: String? = null,
    @SerializedName("entries") val entries: List<Entry> = emptyList(),
) {
  fun stackTraceHeaderToString(entryIndex: Int): String {
    val stackTraceValue = entries[entryIndex].data?.values?.firstOrNull()
    val module = stackTraceValue?.module ?: ""
    val valueMessage = stackTraceValue?.value ?: ""
    val type = stackTraceValue?.type ?: ""
    val stackTraceBuilder = StringBuilder()
    stackTraceBuilder
        .append(module)
        .append(".")
        .append(type)
        .append(": ")
        .append(valueMessage)
        .append("\n")
    return stackTraceBuilder.toString()
  }

  fun stackTraceBodyToString(entryIndex: Int): String {
    val stackTraceValue = entries[entryIndex].data?.values?.firstOrNull()
    val frames = stackTraceValue?.stacktrace?.frames ?: emptyList()
    val stackTraceBuilder = StringBuilder()
    for (i in frames.size - 1 downTo 0) {
      val text =
          "\t${frames[i].module}.${frames[i].function}(${frames[i].filename}:${frames[i].lineNo})"
      stackTraceBuilder.append(text).append("\n")
    }
    return stackTraceBuilder.toString()
  }
}

data class Entry(@SerializedName("data") val data: EntryData? = null)

data class EntryData(@SerializedName("values") val values: List<EntryDataValue> = emptyList())

data class EntryDataValue(
    @SerializedName("stacktrace") val stacktrace: EntryDataValueStacktrace? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("value") val value: String? = null,
    @SerializedName("module") val module: String? = null
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
