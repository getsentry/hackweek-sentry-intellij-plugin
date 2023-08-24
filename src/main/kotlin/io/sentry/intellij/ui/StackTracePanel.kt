package io.sentry.intellij.ui

import com.intellij.ui.components.JBScrollPane
import io.sentry.intellij.models.Frames
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea

class StackTracePanel : JPanel() {
  private val stackTraceTextArea: JTextArea
  private var stackTraceFrames: List<Frames> = emptyList()

  init {
    layout = BorderLayout()
    stackTraceTextArea = JTextArea()
    stackTraceTextArea.isEditable = false
    displaySelectIssueText()

    val scrollPane = JBScrollPane(stackTraceTextArea)
    add(scrollPane, BorderLayout.CENTER)

    scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
    scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS

    displayStackTrace(stackTraceFrames)
  }

  fun updateStackTrace(frames: List<Frames>) {
    stackTraceFrames = frames
    displayStackTrace(stackTraceFrames)
  }

  fun clearText() {
    stackTraceTextArea.text = ""
  }

  /**
   * Display a select issue text when no issue is selected This is the default text of the panel.
   */
  fun displaySelectIssueText() {
    stackTraceTextArea.text = "Select an issue to see the stack trace."
    // TODO Alignment
  }

  fun createStackTraceString(frames: List<Frames>): String {
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
    return stackTraceText.toString()
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
