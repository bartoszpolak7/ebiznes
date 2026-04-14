package io.github.siemamen7.service

import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import java.util.concurrent.CopyOnWriteArrayList

import io.github.siemamen7.data.Database

object BotService {

    @Serializable
    data class DiscordMessage(val author: String, val content: String, val channel: String)

    val messages: MutableList<DiscordMessage> = CopyOnWriteArrayList()

    fun start(token: String) {
        JDABuilder.createDefault(token)
            .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
            .addEventListeners(object : ListenerAdapter() {
                override fun onMessageReceived(event: MessageReceivedEvent) {
                    val content = event.message.contentDisplay
                    val categories = Database.categories.joinToString("\n") { it.name }
                    when {
                        content.startsWith("!categories") -> { event.channel.sendMessage(categories).queue() }
                        content.startsWith("!category") -> {
                            val args = content.split(" ")
                            if (args.size < 2) {
                                event.channel.sendMessage("Usage: !category <name>").queue()
                                return
                            }
                            val categoryName = args[1]
                            val category = Database.categories.find { it.name == categoryName }
                            if (category == null) {
                                event.channel.sendMessage("Category not found: $categoryName").queue()
                                return
                            }
                            val products = Database.products.filter { it.categoryId == category.id }
                                .joinToString("\n") { "${it.name} - ${it.price}" }
                            event.channel.sendMessage("$categoryName:\n$products").queue()
                        }
                        else -> { messages.add(
                                DiscordMessage(
                                    author = event.author.name,
                                    content = event.message.contentDisplay,
                                    channel = event.channel.name
                                )
                            )
                        }
                    }
                }
            })
            .build()
    }
}