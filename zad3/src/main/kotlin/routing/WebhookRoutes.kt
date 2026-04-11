package io.github.siemamen7.routing

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.github.siemamen7.service.WebhookService

import kotlinx.serialization.Serializable

@Serializable
data class MessageRequest(
    val message: String
)

fun Route.webhookRoutes() {

    post("/webhook/send") {

        // Receive JSON body
        val request = call.receive<MessageRequest>()

        // Get webhook URL from environment variable
        val webhookUrl = System.getenv("DISCORD_WEBHOOK_URL")
            ?: error("DISCORD_WEBHOOK_URL not set")

        // Call service
        WebhookService.sendMessage(
            content = request.message,
            webhookUrl = webhookUrl
        )

        // Respond to client
        call.respondText("Message sent to Discord")
    }

    get("/webhook/config") {
        call.respondText("Webhook URL: ${System.getenv("DISCORD_WEBHOOK_URL")}")
    }
}