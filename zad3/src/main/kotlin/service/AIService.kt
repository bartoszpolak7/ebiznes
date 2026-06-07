package io.github.siemamen7.service

import io.github.siemamen7.PythonChatRequest
import io.github.siemamen7.PythonChatResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

object AIService {

  private val client = HttpClient(CIO) {
    install(ContentNegotiation) {
      json()
    }
    install(HttpTimeout) {
      requestTimeoutMillis = 120_000  // 2 minutes
      connectTimeoutMillis = 10_000
      socketTimeoutMillis = 120_000
    }
  }

  private val pythonUrl = System.getenv("PYTHON_SERVICE_URL") ?: "http://localhost:8000"

  suspend fun chat(message: String, newConversation: Boolean, products: List<String>, categories: List<String>): PythonChatResponse {
    return client.post(pythonUrl) {
      contentType(ContentType.Application.Json)
      setBody(
        PythonChatRequest(
          message = message,
          new_conversation = newConversation,
          products = products,
          categories = categories
        )
      )
    }.body()
  }
}