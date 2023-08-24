package io.sentry.intellij.ui

import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import io.sentry.intellij.models.IssueEvent

fun ConsoleView.printSentryStackTrace(issueEvent: IssueEvent) {
  // TODO index is not always 0, it's also possible to have multiple stack traces
  print(issueEvent.stackTraceHeaderToString(0), ConsoleViewContentType.ERROR_OUTPUT)
  print(issueEvent.stackTraceBodyToString(0), ConsoleViewContentType.NORMAL_OUTPUT)
}
