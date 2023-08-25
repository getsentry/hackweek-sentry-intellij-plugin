package io.sentry.intellij.http

import com.google.gson.Gson
import io.sentry.intellij.models.Issue
import io.sentry.intellij.models.IssueEvent
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import okhttp3.*

class ApiService(
  private val client: OkHttpClient,
  private val orgSlug: String,
  private val projectSlug: String,
  authToken: String
) {
  private val baseUrl = "https://sentry.io/api/0/"
  private val gson = Gson()
  private val reqBuilder = Request.Builder().addHeader("Authorization", "Bearer $authToken")

  suspend fun fetchIssues() =
    // TODO: Specify levels and separate error and fatal
    suspendCoroutine<Array<Issue>> { continuation ->
      val request =
        reqBuilder
          .url("${baseUrl}projects/$orgSlug/$projectSlug/issues/?query=level:error")
          .get()
          .build()

      val call = client.newCall(request)
      call.enqueue(
        object : Callback {
          override fun onFailure(call: Call, e: IOException) {
            // TODO: Handle error gracefully
            println("Failed to execute request: ${e.message}")
            continuation.resume(emptyArray())
          }

          override fun onResponse(call: Call, response: Response) {
            if (response.code == 200) {
              val issues = gson.fromJson(response.body?.string(), Array<Issue>::class.java)
              continuation.resume(issues)
            } else {
              println("Response failed with status: ${response.code}")
              continuation.resume(emptyArray())
            }
          }
        })
    }

  suspend fun fetchLatestIssueEvent(issueId: String) = suspendCoroutine { continuation ->
    val request = reqBuilder.url("${baseUrl}issues/$issueId/events/latest/").get().build()

    val call = client.newCall(request)
    call.enqueue(
      object : Callback {
        override fun onFailure(call: Call, e: IOException) {
          println("Failed to execute request: ${e.message}")
          continuation.resume(null)
        }

        override fun onResponse(call: Call, response: Response) {
          val issueEvent = gson.fromJson(response.body?.string(), IssueEvent::class.java)
          continuation.resume(issueEvent)
        }
      })
  }
}
