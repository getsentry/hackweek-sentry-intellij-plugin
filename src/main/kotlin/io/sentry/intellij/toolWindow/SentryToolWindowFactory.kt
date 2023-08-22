package io.sentry.intellij.toolWindow

import com.google.gson.Gson
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ThreeComponentsSplitter
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLoadingPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import io.sentry.intellij.models.Frames
import io.sentry.intellij.models.Issue
import io.sentry.intellij.ui.IssuesOverviewPanel
import java.awt.BorderLayout
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.swing.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SentryToolWindowFactory : ToolWindowFactory, DumbAware {
  override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
    val toolWindowContent = CalendarToolWindowContent(toolWindow)
    val content =
        ContentFactory.getInstance().createContent(toolWindowContent.contentPanel, "", false)
    toolWindow.contentManager.addContent(content)
  }

  private class CalendarToolWindowContent(toolWindow: ToolWindow) : Disposable {
    // TODO: this should not be hardcoded!
    private val authToken = "..."

    val loadingPanel =
        JBLoadingPanel(BorderLayout(), toolWindow.disposable).apply { layout = BorderLayout() }
    val component2 = JPanel().apply { background = JBColor.BLUE }
    val component3 = JPanel().apply { background = JBColor.RED }

    private val splitter =
        ThreeComponentsSplitter(toolWindow.disposable).apply {
          val windowSize = toolWindow.component.size
          firstSize = windowSize.width / 4
          lastSize = windowSize.width / 4
          firstComponent = loadingPanel
          innerComponent = component2
          lastComponent = component3
        }

    val contentPanel: JComponent =
        JPanel().apply {
          layout = BorderLayout()
          add(splitter, BorderLayout.CENTER)
        }

    init {
      getFromSentry()
    }

    private fun getFromSentry() {
      val gson = Gson()

      loadingPanel.startLoading()
      GlobalScope.launch {
        val issueArrays: Array<Issue> =
            gson.fromJson(
                request("https://sentry.io/api/0/projects/sentry-sdks/sentry-android/issues/"),
                Array<Issue>::class.java)
        val issuesOverviewPanel = IssuesOverviewPanel(issueArrays.toList())
        val scrollPane = JBScrollPane(issuesOverviewPanel)

        /*
        val issueEvents = eventArray.take(1).map {
            gson.fromJson(
                request("https://sentry.io/api/0/issues/${it.id}/events/latest/"),
                IssueEvent::class.java
            )
        }
        issueEvents.forEach {
            val frames = it.entries[1].data?.values?.get(0)?.stacktrace?.frames
            frames?.let {
                val stackTracePanel = StackTracePanel(it)
                val stackTraceScrollPane = JBScrollPane(stackTracePanel)
                stackTraceScrollPane.maximumSize = Dimension(500, Int.MAX_VALUE)

                //contentPanel.rightComponent = stackTraceScrollPane // Set right component
            }
        }*/
        println("eventArray: $issueArrays")
        loadingPanel.add(scrollPane, BorderLayout.CENTER)
        loadingPanel.stopLoading()
      }
    }

    private fun request(urlString: String): String? {
      try {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        // Set request method (GET, POST, etc.)
        connection.requestMethod = "GET"

        // Set the Authorization header with Bearer token
        connection.setRequestProperty("Authorization", "Bearer $authToken")
        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
          val `in` = BufferedReader(InputStreamReader(connection.inputStream))
          var inputLine: String?
          val response = StringBuilder()
          while (`in`.readLine().also { inputLine = it } != null) {
            response.append(inputLine)
          }
          `in`.close()
          return response.toString()
        } else {
          println("HTTP Request failed with response code: $responseCode")
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
      return null
    }

    override fun dispose() {
      TODO("Not yet implemented")
    }
  }
}

class StackTracePanel(stackTraceFrames: List<Frames>) : JPanel() {
  private val stackTraceTextArea: JTextArea

  init {
    layout = BorderLayout()
    setSize(500, 500)
    stackTraceTextArea = JTextArea()
    stackTraceTextArea.isEditable = false

    val scrollPane = JBScrollPane(stackTraceTextArea)
    add(scrollPane, BorderLayout.CENTER)

    scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
    scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS

    // TODO: Scroll
    displayStackTrace(stackTraceFrames)
  }

  private fun displayStackTrace(frames: List<Frames>) {
    val stackTraceText = StringBuilder()
    for (i in frames.size - 1 downTo 0) {
      var tab = "\t"
      if (i == frames.size - 1) {
        tab = ""
      }
      val text =
          "${tab}${frames[i].module}.${frames[i].function}(${frames[i].filename}:${frames[i].lineNo})"
      stackTraceText.append(text).append("\n")
    }
    stackTraceTextArea.text = stackTraceText.toString()
  }
}

fun createMainSplitter(disposable: Disposable): ThreeComponentsSplitter {
  val mainSplitter =
      ThreeComponentsSplitter(disposable).apply {
        val loadingPanel = JPanel().apply { background = JBColor.LIGHT_GRAY }
        val component2 = JPanel().apply { background = JBColor.BLUE }
        val component3 = JPanel().apply { background = JBColor.YELLOW }
        firstSize = 100
        lastSize = 100
        firstComponent = loadingPanel
        innerComponent = component2
        lastComponent = component3
      }
  return mainSplitter
}
