package io.github.siemamen7.service

import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import java.util.concurrent.CopyOnWriteArrayList

object BotService {

    @Serializable
    data class DiscordMessage(val author: String, val content: String, val channel: String)

    val messages: MutableList<DiscordMessage> = CopyOnWriteArrayList()

    fun start(token: String) {
        JDABuilder.createDefault(token)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
            .addEventListeners(object : ListenerAdapter() {
                override fun onMessageReceived(event: MessageReceivedEvent) {
                    if (event.author.isBot) return
                    messages.add(
                        DiscordMessage(
                            author = event.author.name,
                            content = event.message.contentDisplay,
                            channel = event.channel.name
                        )
                    )
                }
            })
            .build()
    }
}
