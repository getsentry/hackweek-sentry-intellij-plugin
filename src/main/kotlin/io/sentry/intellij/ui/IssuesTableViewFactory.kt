package io.sentry.intellij.ui

import com.intellij.ui.table.TableView
import com.intellij.util.ui.ListTableModel
import io.sentry.intellij.models.Issue

object IssuesTableViewFactory : TableViewFactory<Issue> {
  override fun create(
      listTableModel: ListTableModel<Issue>,
      preferredWidths: List<Int>
  ): TableView<Issue> {
    return CommonTableView(listTableModel).apply {
      preferredWidths.forEachIndexed { index, preferredWidth ->
        columnModel.getColumn(index).preferredWidth = preferredWidth
      }
    }
  }

  override fun create(listTableModel: ListTableModel<Issue>): TableView<Issue> {
    return create(listTableModel, emptyList())
  }
}
