package io.sentry.intellij.http

import okhttp3.OkHttpClient

object OkHttpClientProvider {
  // Configure the client with interceptors etc...
  private val client: OkHttpClient by lazy { OkHttpClient.Builder().build() }

  fun provideClient(): OkHttpClient {
    return client
  }
}
