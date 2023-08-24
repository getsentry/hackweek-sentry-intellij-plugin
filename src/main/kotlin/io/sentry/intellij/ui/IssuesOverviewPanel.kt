package io.sentry.intellij.ui

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.TableView
import io.sentry.intellij.models.Issue
import java.awt.BorderLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel
import javax.swing.SwingConstants.CENTER

class IssuesOverviewPanel : JPanel() {
  var onIssueSelected: ((Issue) -> Unit)? = null
  private var tableView: TableView<Issue>? = null

  init {
    layout = BorderLayout()
  }

  fun updateIssues(issues: Array<Issue>) {
    if (issues.isEmpty()) {
      val label = JBLabel("No issues could be found.").apply { horizontalAlignment = CENTER }
      add(label)
    } else {
      if (tableView != null) {
        tableView?.model = IssuesListTableModelFactory.create(issues.toList())
      } else {
        tableView = createIssuesOverviewTable(issues)
        val scrollPane = JBScrollPane(tableView)
        add(scrollPane)
      }
    }
  }

  private fun createIssuesOverviewTable(issues: Array<Issue>): TableView<Issue> {
    val tableModel = IssuesListTableModelFactory.create(issues.toList())
    val preferredWidths = listOf(200, 50, 50)
    val tableView = IssuesTableViewFactory.create(tableModel, preferredWidths)
    tableView.addMouseListener(
        object : MouseAdapter() {
          override fun mouseClicked(e: MouseEvent?) {
            super.mouseClicked(e)
            val selectedIssue = tableView.getRow(tableView.selectedRow)
            onIssueSelected?.invoke(selectedIssue)
          }
        })
    return tableView
  }
}
