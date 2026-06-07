package io.github.siemamen7
import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
  val message: String,
  val newConversation: Boolean = false
)

@Serializable
data class PythonChatRequest(
  val message: String,
  val new_conversation: Boolean = false,
  val products: List<String>,
  val categories: List<String>
)

@Serializable
data class PythonChatResponse(
  val response: String,
  val is_opening: Boolean
)

data class Category(val id: Int, val name: String)
data class Product(val id: Int, val name: String, val price: Double, val categoryId: Int)