package io.sentry.intellij.ui

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.TableView
import io.sentry.intellij.models.Issue
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.SwingConstants.CENTER

class IssuesOverviewPanel(issues: List<Issue> = emptyList()) : JPanel() {
  init {
    layout = BorderLayout()
    if (issues.isEmpty()) {
      val label = JBLabel("No issues could be found.").apply { horizontalAlignment = CENTER }
      add(label)
    } else {
      val tableView = createIssuesOverviewTable(issues)
      val scrollPane = JBScrollPane(tableView)
      add(scrollPane)
    }
  }

  private fun createIssuesOverviewTable(issues: List<Issue>): TableView<Issue> {
    val tableModel = IssuesListTableModelFactory.create(issues)
    val preferredWidths = listOf(200, 50, 50)
    return IssuesTableViewFactory.create(tableModel, preferredWidths)
  }
}
