package io.github.siemamen7.backend

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class ProductsApiTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun getProductsReturnsThreeItems() {
        val response = restTemplate.getForEntity("/products", Array<Product>::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        val products = requireNotNull(response.body)
        assertEquals(3, products.size)
        assertEquals(1, products[0].id)
        assertEquals("Dziadek do orzechów", products[0].name)
        assertEquals(29.99, products[0].price)
        assertEquals(2, products[1].id)
        assertEquals("Lego Ninjago", products[1].name)
        assertEquals(49.99, products[1].price)
        assertEquals(3, products[2].id)
        assertEquals("Buty na rzepy", products[2].name)
        assertEquals(99.99, products[2].price)
    }

    @Test
    fun postProductsIsNotAllowed() {
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val entity = HttpEntity("""{"name":"test"}""", headers)

        val response = restTemplate.exchange("/products", HttpMethod.POST, entity, String::class.java)

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.statusCode)
    }
}
