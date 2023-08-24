package io.sentry.intellij.ui

import com.intellij.ui.table.TableView
import com.intellij.util.ui.ListTableModel
import io.sentry.intellij.models.Issue

object IssuesTableViewFactory : TableViewFactory<Issue> {
  override fun create(
      listTableModel: ListTableModel<Issue>,
      preferredWidths: List<Int>
  ): TableView<Issue> {
    val tableView =
        CommonTableView(listTableModel).apply {
          // TODO: can refactor this to use key, value pairs
          preferredWidths.forEachIndexed { index, preferredWidth ->
            if (index >= 1) {
              columnModel.getColumn(index).maxWidth = preferredWidth
            }
            columnModel.getColumn(index).preferredWidth = preferredWidth
          }
          // Select the first row by default which is the first issue from which events are loaded
          if (listTableModel.rowCount > 0) {
            selectionModel.setSelectionInterval(0, 0)
          }
        }
    return tableView
  }

  override fun create(listTableModel: ListTableModel<Issue>): TableView<Issue> {
    return create(listTableModel, emptyList())
  }
}
