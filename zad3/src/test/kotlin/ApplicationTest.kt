package io.github.siemamen7

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.github.siemamen7.routing.configureRouting

class ApplicationTest {
    @Test
    fun testPostWebhookSend() = testApplication {
        application {
            configureRouting()
        }
        val response = client.post("/webhook/send") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("{\"message\":\"Hello Discord\"}")
        }
        // This is expected to fail with UnsupportedMediaType or ContentTransformationException because server lacks ContentNegotiation
        println("[DEBUG_LOG] Status: ${response.status}")
    }
}
