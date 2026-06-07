package io.github.siemamen7.service

import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import java.util.concurrent.CopyOnWriteArrayList

import io.github.siemamen7.data.Database
import kotlinx.coroutines.launch

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
            // !kategorie
            content.startsWith("!kategorie") -> {
              event.channel.sendMessage(categories).queue()
            }

            // !kategoria <nazwa>
            content.startsWith("!kategoria") -> {
              val args = content.split(" ")
              if (args.size < 2) {
                event.channel.sendMessage("Użytkowanie: !kategoria <nazwa>").queue()
                return
              }
              val categoryName = args[1]
              val category = Database.categories.find { it.name == categoryName }
              if (category == null) {
                event.channel.sendMessage("Nie znaleziono kategorii: $categoryName").queue()
                return
              }
              val products = Database.products.filter { it.categoryId == category.id }
                .joinToString("\n") { "${it.name} - ${it.price}" }
              event.channel.sendMessage("$categoryName:\n$products").queue()
            }

            // !czat
            content.startsWith("!czat") -> {
              val msg = content.removePrefix("!czat").trim()
              if (msg.isEmpty()) {
                event.channel.sendMessage("Użytkowanie: !czat <wiadomość>").queue()
                return
              }
              val products = Database.products.map { "${it.name} - ${it.price}" }
              val categories = Database.categories.map { it.name }

              event.channel.sendTyping().queue()

              kotlinx.coroutines.GlobalScope.launch {
                try {
                  val response = AIService.chat(
                    message = msg,
                    newConversation = false,
                    products = products,
                    categories = categories
                  )
                  event.channel.sendMessage(response.response).queue()
                } catch (e: Exception) {
                  event.channel.sendMessage("Błąd: ${e.message}").queue()
                }
              }

            }

            // nie znaleziono komendy
            content.startsWith("!") -> {
              return event.channel.sendMessage("Nie znaleziono komendy.").queue()
            }
          }
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