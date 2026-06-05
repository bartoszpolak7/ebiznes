package io.github.siemamen7

import io.github.siemamen7.routing.routes
import io.github.siemamen7.service.BotService
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val botToken = System.getenv("DISCORD_BOT_TOKEN") ?: error("DISCORD_BOT_TOKEN not set")
    BotService.start(botToken)
    configureRouting()
}

fun Application.configureRouting() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        routes()
        staticResources("/", "static")
    }
}