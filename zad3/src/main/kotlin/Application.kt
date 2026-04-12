package io.github.siemamen7

import io.github.siemamen7.routing.configureRouting
import io.github.siemamen7.service.BotService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val botToken = System.getenv("DISCORD_BOT_TOKEN") ?: error("DISCORD_BOT_TOKEN not set")
    BotService.start(botToken)
    configureRouting()
}
