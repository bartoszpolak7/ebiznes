package io.github.siemamen7.backend

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class PaymentsApiTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun postPaymentResponseIsNotNull() {
        val response = postPayment("""{"total": 29.99}""")

        assertNotNull(response)
        assertNotNull(response.statusCode)
    }

    @Test
    fun postPaymentsWithValidTotalReturnsOk() {
        val response = postPayment("""{"total":79.98}""")

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals("ok", response.body?.get("status"))
    }

    @Test
    fun postPaymentsWithZeroTotalReturnsBadRequest() {
        val response = postPayment("""{"total":0}""")

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("error", response.body?.get("status"))
    }

    @Test
    fun postPaymentsWithNegativeTotalReturnsBadRequest() {
        val response = postPayment("""{"total":-10.5}""")

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("invalid payment payload", response.body?.get("message"))
    }

    @Test
    fun postPaymentsWithoutTotalReturnsBadRequest() {
        val response = postPayment("""{}""")

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(400, response.body?.get("status"))
    }

    private fun postPayment(body: String) =
        restTemplate.postForEntity(
            "/payments",
            HttpEntity(body, HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }),
            Map::class.java
        )
}
