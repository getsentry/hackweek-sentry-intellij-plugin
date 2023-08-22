package io.sentry.intellij.ui

import com.intellij.ui.table.TableView
import com.intellij.util.ui.ListTableModel
import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.JTableHeader

/**
 * A table view that is used in multiple places in the plugin. This class provides the common
 * properties of the desired table view UI.
 */
class CommonTableView<T>(listTableModel: ListTableModel<T>) : TableView<T>(listTableModel) {
  init {
    setShowGrid(false)

    // Only one issue at a time can be selected
    selectionModel.selectionMode = ListSelectionModel.SINGLE_SELECTION

    // Header based on the column model
    tableHeader =
        JTableHeader(columnModel).apply {
          reorderingAllowed = false
          resizingAllowed = false
        }

    // Apply the custom cell renderer to each cell
    for (i in 0 until columnCount) {
      columnModel.getColumn(i).cellRenderer = defaultCellRenderer()
    }
  }

  /**
   * Custom cell renderer that disables focus so that the individual cell is not highlighted when
   * selected. This is done to make the selected row look more uniform.
   */
  private fun defaultCellRenderer(): DefaultTableCellRenderer =
      object : DefaultTableCellRenderer() {
        override fun getTableCellRendererComponent(
            table: JTable?,
            value: Any?,
            isSelected: Boolean,
            hasFocus: Boolean,
            row: Int,
            column: Int
        ) = super.getTableCellRendererComponent(table, value, isSelected, false, row, column)
      }
}
