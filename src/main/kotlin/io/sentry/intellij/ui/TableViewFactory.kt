package io.sentry.intellij.ui

import com.intellij.ui.table.TableView
import com.intellij.util.ui.ListTableModel

interface TableViewFactory<T> {
  /**
   * Creates a table view with the given preferred widths for the columns. Beware that the preferred
   * widths have to be sorted in the same order as the columns are sorted in the model.
   */
  fun create(listTableModel: ListTableModel<T>, preferredWidths: List<Int>): TableView<T>

  /** Creates a table view with the default preferred widths for the columns. */
  fun create(listTableModel: ListTableModel<T>): TableView<T>
}
