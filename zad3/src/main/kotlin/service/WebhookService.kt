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
    // Get webhook URL from environment variable
    val webhookUrl = System.getenv("DISCORD_WEBHOOK_URL")
        ?: error("DISCORD_WEBHOOK_URL not set")

    suspend fun sendMessage(content: String) {
        client.post(webhookUrl) {
            contentType(ContentType.Application.Json)
            setBody(DiscordMessage(content))
        }
    }
}