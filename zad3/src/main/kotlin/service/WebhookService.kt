package io.github.siemamen7.service

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable

@Serializable
data class DiscordMessage(val content: String)

object WebhookService {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun sendMessage(content: String, webhookUrl: String) {
        client.post(webhookUrl) {
            contentType(ContentType.Application.Json)
            setBody(DiscordMessage(content))
        }
    }
}