package io.github.siemamen7.routing

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.github.siemamen7.service.WebhookService
import io.github.siemamen7.service.BotService

import kotlinx.serialization.Serializable

@Serializable
data class MessageRequest(
    val message: String
)

fun Route.routes() {

    post("/webhook/send") {

        // Receive JSON body
        val request = call.receive<MessageRequest>()

        // Call service
        WebhookService.sendMessage(content = request.message)

        // Respond to client
        call.respondText("Message sent to Discord")
    }

    get("/webhook/config") {
        call.respondText("Webhook URL: ${System.getenv("DISCORD_WEBHOOK_URL")}")
    }

    get("/bot/messages") {
        call.respond(BotService.messages)
    }
}