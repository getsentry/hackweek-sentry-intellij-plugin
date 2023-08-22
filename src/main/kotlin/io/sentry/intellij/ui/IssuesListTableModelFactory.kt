package io.sentry.intellij.ui

import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.ListTableModel
import io.sentry.intellij.models.Issue

object IssuesListTableModelFactory : ListTableModelFactory<Issue> {
  private const val issuesLabel = "Issues"
  private const val eventCountLabel = "Events"
  private const val userCountLabel = "Users"

  override fun create(list: List<Issue>): ListTableModel<Issue> {
    return ListTableModel<Issue>(
            IssuesNameColumn(issuesLabel),
            EventCountColumn(eventCountLabel),
            UserCountColumn(userCountLabel))
        .apply { addRows(list) }
  }

  private class IssuesNameColumn(label: String) : ColumnInfo<Issue, String>(label) {
    override fun valueOf(item: Issue): String? {
      return item.title
    }
  }

  private class EventCountColumn(label: String) : ColumnInfo<Issue, String>(label) {
    override fun valueOf(item: Issue): String? {
      return item.count
    }
  }

  private class UserCountColumn(label: String) : ColumnInfo<Issue, String>(label) {
    override fun valueOf(item: Issue): String? {
      return item.userCount?.toString()
    }
  }
}
