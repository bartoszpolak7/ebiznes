package io.github.siemamen7.service

import io.github.siemamen7.PythonChatRequest
import io.github.siemamen7.PythonChatResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
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
  }

  suspend fun chat(message: String, newConversation: Boolean, products: List<String>, categories: List<String>): PythonChatResponse {
    return client.post("http://localhost:8000/chat") {
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