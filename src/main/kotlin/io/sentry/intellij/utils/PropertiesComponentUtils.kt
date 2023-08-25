package io.sentry.intellij.utils

import com.intellij.ide.util.PropertiesComponent

object PropertiesComponentUtils {
  private const val AUTH_TOKEN_KEY = "sentry_auth_token"
  private const val ORG_SLUG_KEY = "sentry_org_slug"
  private const val PROJECT_SLUG_KEY = "sentry_project_slug"

  var authToken: String?
    set(value) = PropertiesComponent.getInstance().setValue(AUTH_TOKEN_KEY, value)
    get() = PropertiesComponent.getInstance().getValue(AUTH_TOKEN_KEY)

  var orgSlug: String?
    set(value) = PropertiesComponent.getInstance().setValue(ORG_SLUG_KEY, value)
    get() = PropertiesComponent.getInstance().getValue(ORG_SLUG_KEY)

  var projectSlug: String?
    set(value) = PropertiesComponent.getInstance().setValue(PROJECT_SLUG_KEY, value)
    get() = PropertiesComponent.getInstance().getValue(PROJECT_SLUG_KEY)
}
