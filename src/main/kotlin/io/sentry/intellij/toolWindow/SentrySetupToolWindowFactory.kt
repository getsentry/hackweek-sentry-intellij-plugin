package io.sentry.intellij.toolWindow

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.ui.content.ContentFactory
import io.sentry.intellij.utils.PropertiesComponentUtils
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import javax.swing.*
import javax.swing.event.DocumentListener

/**
 * This class is responsible for creating the Sentry Setup tool window.
 *
 * The Sentry Setup tool window is used to connect to a Sentry project. It will require the user to
 * enter an authentication token, organization slug, and project slug.
 */
class SentrySetupToolWindowFactory : ToolWindowFactory, DumbAware {
  override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
    val toolWindowContent = SentrySetupToolWindowContent(project, toolWindow)
    val content =
        ContentFactory.getInstance().createContent(toolWindowContent.contentPanel, "", false)
    toolWindow.contentManager.addContent(content)
  }

  private class SentrySetupToolWindowContent(
      project: Project,
      toolWindow: ToolWindow,
  ) {
    val contentPanel: JComponent

    init {
      contentPanel = JPanel(BorderLayout())
      val bo = Bo()
      bo.onConnectButtonClicked = {
        val authToken = bo.authTokenContainer.textFieldText
        val orgSlug = bo.orgSlugContainer.textFieldText
        val projectSlug = bo.projectSlugContainer.textFieldText
        if (authToken != "" && orgSlug != "" && projectSlug != "") {
          PropertiesComponentUtils.authToken = authToken
          PropertiesComponentUtils.orgSlug = orgSlug
          PropertiesComponentUtils.projectSlug = projectSlug
          bo.isSuccessLabelVisible = true
        }
      }
      contentPanel.add(bo, BorderLayout.CENTER)
    }
  }
}

class HeaderTextFieldContainer(labelText: String) : JPanel() {
  var textFieldText = ""
    private set

  init {
    layout = BoxLayout(this, BoxLayout.Y_AXIS)

    add(JBLabel(labelText).apply { alignmentX = Component.LEFT_ALIGNMENT })

    // Add vertical spacing
    add(Box.createVerticalStrut(5))

    add(
        JBTextField().apply {
          alignmentX = Component.LEFT_ALIGNMENT
          preferredSize = Dimension(220, 30)
          maximumSize = Dimension(220, 30)
          document.addDocumentListener(
              object : DocumentListener {
                override fun insertUpdate(e: javax.swing.event.DocumentEvent?) {
                  textFieldText = text
                }

                override fun removeUpdate(e: javax.swing.event.DocumentEvent?) {
                  textFieldText = text
                }

                override fun changedUpdate(e: javax.swing.event.DocumentEvent?) {}
              })
        })
  }
}

class Bo : JPanel() {
  private val successLabel =
      JBLabel("Connection successful").apply {
        isVisible = false
        foreground = JBColor.GREEN
      }

  var onConnectButtonClicked: (() -> Unit)? = null
  var isSuccessLabelVisible = false
    set(value) {
      field = value
      successLabel.isVisible = value
    }

  val authTokenContainer: HeaderTextFieldContainer
  val orgSlugContainer: HeaderTextFieldContainer
  val projectSlugContainer: HeaderTextFieldContainer

  init {
    layout = BoxLayout(this, BoxLayout.Y_AXIS)
    border = BorderFactory.createEmptyBorder(10, 10, 0, 0)
    authTokenContainer = HeaderTextFieldContainer("Authentication Token")
    add(authTokenContainer)
    orgSlugContainer = HeaderTextFieldContainer("Organization Slug")
    add(orgSlugContainer)
    projectSlugContainer = HeaderTextFieldContainer("Project Slug")
    add(projectSlugContainer)
    val connectButton = JButton("Connect")
    connectButton.addActionListener { onConnectButtonClicked?.invoke() }
    add(connectButton)
    add(successLabel)
  }
}
