package io.sentry.intellij.ui

import com.intellij.util.ui.ListTableModel

interface ListTableModelFactory<T> {
  fun create(list: List<T>): ListTableModel<T>
}
