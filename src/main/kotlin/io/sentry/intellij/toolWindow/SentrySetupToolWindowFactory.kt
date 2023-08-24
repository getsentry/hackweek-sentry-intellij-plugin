package io.sentry.intellij.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import com.intellij.ui.content.ContentFactory
import io.sentry.intellij.http.ApiService
import io.sentry.intellij.http.OkHttpClientProvider
import java.awt.BorderLayout
import javax.swing.*

class SentrySetupToolWindowFactory : ToolWindowFactory {
  override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
    val orgSlug = "sentry-sdks"
    val projectSlug = "sentry-android"
    val apiService = ApiService(OkHttpClientProvider.provideClient(), orgSlug, projectSlug)
    val toolWindowContent = SentrySetupToolWindowContent(project, toolWindow, apiService)
    val content =
        ContentFactory.getInstance().createContent(toolWindowContent.contentPanel, "", false)
    toolWindow.contentManager.addContent(content)
  }

  private class SentrySetupToolWindowContent(
      project: Project,
      toolWindow: ToolWindow,
      private val apiService: ApiService
  ) {
    val contentPanel: JComponent

    init {
      contentPanel = JPanel().apply { layout = BorderLayout() }
      val scrollPane = JBScrollPane()

      // Header 1
      val headerLabel1 = JBLabel("Header 1")
      contentPanel.add(headerLabel1)

      // Text Field 1
      val textField1 = JBTextField(15)
      contentPanel.add(textField1)

      // Header 2
      val headerLabel2 = JBLabel("Header 2")
      contentPanel.add(headerLabel2)

      // Text Field 2
      val textField2 = JBTextField(15)
      contentPanel.add(textField2)

      // Button
      val button = JButton("Submit")
      contentPanel.add(button)
    }
  }
}
