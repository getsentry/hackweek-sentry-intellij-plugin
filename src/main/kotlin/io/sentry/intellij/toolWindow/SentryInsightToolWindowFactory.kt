package io.sentry.intellij.toolWindow

import com.intellij.execution.impl.ConsoleViewImpl
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ThreeComponentsSplitter
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.components.JBLoadingPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import io.sentry.intellij.http.ApiService
import io.sentry.intellij.http.OkHttpClientProvider
import io.sentry.intellij.models.Issue
import io.sentry.intellij.ui.IssuesOverviewPanel
import io.sentry.intellij.ui.printSentryStackTrace
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SentryInsightToolWindowFactory : ToolWindowFactory {
  override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
    val toolWindowContent = SentryInsightToolWindowContent(project, toolWindow)
    val content =
      ContentFactory.getInstance().createContent(toolWindowContent.contentPanel, "", false)
    toolWindow.contentManager.addContent(content)
  }

  private class SentryInsightToolWindowContent(
    project: Project,
    toolWindow: ToolWindow,
  ) {
    // First (left-most) panel that displays the issues overview
    private val firstLoadingPanel: JBLoadingPanel

    // Inner panel that displays the stack trace of the selected issue
    private val innerLoadingPanel: JBLoadingPanel

    // Last (right-most) panel that displays metadata about the selected issue (tags, context, device, etc...)
    private val metadataPanel: JPanel
    private var consoleView: ConsoleViewImpl
    private var issuesOverviewPanel: IssuesOverviewPanel
    private var selectedIssue: Issue? = null
    private val splitter: ThreeComponentsSplitter
    private var apiService: ApiService? = null
    val contentPanel: JComponent

    init {
      firstLoadingPanel = createLoadingPanel(toolWindow.disposable)
      innerLoadingPanel = createLoadingPanel(toolWindow.disposable)
      issuesOverviewPanel =
        createIssuesOverviewPanel().also {
          val scrollPane = JBScrollPane(it)
          firstLoadingPanel.add(scrollPane, BorderLayout.CENTER)
        }
      consoleView =
        createConsoleView(project).also {
          innerLoadingPanel.add(it.component, BorderLayout.CENTER)
        }
      metadataPanel = createMetadataPanel()
      val windowManager = WindowManager.getInstance()
      val mainFrame = windowManager.getIdeFrame(project)
      val windowSize = mainFrame?.component?.size
      val windowWidth = windowSize?.width ?: 200
      splitter =
        createSplitter(
          firstLoadingPanel,
          innerLoadingPanel,
          metadataPanel,
          toolWindow,
          windowWidth / 4,
          windowWidth / 4
        )
      contentPanel = createContentPanel(splitter)
      // TODO: This should not hardcoded but cached in the Setup toolWindow
      val authToken = "..."
      val orgSlug = "sentry-sdks"
      val projectSlug = "sentry-android"
      if (authToken == null || orgSlug == null || projectSlug == null) {
        // TODO: show a message to the user that they need to set up the project connection
      } else {
        apiService = ApiService(OkHttpClientProvider.provideClient(), orgSlug, projectSlug, authToken)
        GlobalScope.launch { loadData() }
      }
    }

    private fun createLoadingPanel(disposable: Disposable): JBLoadingPanel {
      return JBLoadingPanel(BorderLayout(), disposable).apply { layout = BorderLayout() }
    }

    private fun createConsoleView(project: Project): ConsoleViewImpl {
      return ConsoleViewImpl(project, true)
    }

    private fun createMetadataPanel(): JPanel {
      return JPanel()
    }

    private fun createSplitter(
      loadingPanel: JBLoadingPanel,
      innerLoadingPanel: JBLoadingPanel,
      component3: JPanel,
      toolWindow: ToolWindow,
      firstSize: Int,
      lastSize: Int,
    ): ThreeComponentsSplitter {
      val splitter =
        ThreeComponentsSplitter(toolWindow.disposable).apply {
          this@apply.firstSize = firstSize
          this@apply.lastSize = lastSize
          firstComponent = loadingPanel
          innerComponent = innerLoadingPanel
          lastComponent = component3
        }
      return splitter
    }

    private fun createContentPanel(splitter: ThreeComponentsSplitter): JComponent {
      return JPanel().apply {
        layout = BorderLayout()
        add(splitter, BorderLayout.CENTER)
      }
    }

    /**
     * Complete (total) loading of issues and issue events. This is used during initialization but
     * can also be used to refresh all data.
     */
    private suspend fun loadData() {
      suspendingTotalLoadingSequence {
        apiService?.let {
          val issues = it.fetchIssues()
          issuesOverviewPanel.updateIssues(issues)
          loadInitialIssueEvent(issues)
        }
      }
    }

    private fun createIssuesOverviewPanel(): IssuesOverviewPanel {
      val issuesOverviewPanel = IssuesOverviewPanel()
      issuesOverviewPanel.onIssueSelected = ::handleIssueSelected
      return issuesOverviewPanel
    }

    /**
     * Loads the latest issue event for the first issue in the list. This is only done once when the
     * tool window is opened.
     */
    private suspend fun loadInitialIssueEvent(issues: Array<Issue>) {
      if (selectedIssue == null && issues.isNotEmpty()) {
        selectedIssue = issues.first()
        loadIssueEvent(issues.first().id)
      }
    }

    /** Loads the latest issue event for the selected issue. */
    private suspend fun loadIssueEvent(issueId: String) {
      consoleView.clear()
      innerLoadingPanel.startLoading()
      val latestIssueEvent = apiService?.fetchLatestIssueEvent(issueId)
      latestIssueEvent?.let { consoleView.printSentryStackTrace(it) }
      innerLoadingPanel.stopLoading()
    }

    private fun handleIssueSelected(issue: Issue) {
      if (issue != selectedIssue) {
        selectedIssue = issue
        GlobalScope.launch { loadIssueEvent(issue.id) }
      }
    }

    /** Starts the total loading state of the tool window. */
    private fun startTotalLoading() {
      firstLoadingPanel.startLoading()
      innerLoadingPanel.startLoading()
    }

    /** Stops the total loading state of the tool window. */
    private fun stopTotalLoading() {
      firstLoadingPanel.stopLoading()
      innerLoadingPanel.stopLoading()
    }

    /**
     * Kotlin idiomatic way to wrap any code that requires total loading state during execution of a
     * code-block.
     */
    private suspend fun suspendingTotalLoadingSequence(action: suspend () -> Unit) {
      startTotalLoading()
      action()
      stopTotalLoading()
    }
  }
}
