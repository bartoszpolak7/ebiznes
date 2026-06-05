package io.github.siemamen7.backend
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ProductCatalogTest {

    private val catalog = ProductCatalog()

    @Test
    fun getProductsReturnsExactlyThreeItems() {
        val products = catalog.getProducts()
        assertEquals(3, products.size)
        assertEquals(1, products[0].id)
        assertEquals(2, products[1].id)
        assertEquals(3, products[2].id)
    }

    @Test
    fun getProductsContainsExpectedNames() {
        val names = catalog.getProducts().map { it.name }
        assertTrue(names.contains("Dziadek do orzechów"))
        assertTrue(names.contains("Lego Ninjago"))
        assertTrue(names.contains("Buty na rzepy"))
        assertFalse(names.contains("Unknown product"))
    }

    @Test
    fun getProductsContainsExpectedPrices() {
        val prices = catalog.getProducts().map { it.price }
        assertTrue(prices.contains(29.99))
        assertTrue(prices.contains(49.99))
        assertTrue(prices.contains(99.99))
        assertFalse(prices.contains(0.0))
    }

    @Test
    fun findByIdReturnsProductForExistingIds() {
        val first = catalog.findById(1)
        val second = catalog.findById(2)
        val third = catalog.findById(3)

        assertNotNull(first)
        assertNotNull(second)
        assertNotNull(third)
        assertEquals("Dziadek do orzechów", first!!.name)
        assertEquals("Lego Ninjago", second!!.name)
        assertEquals("Buty na rzepy", third!!.name)
        assertEquals(29.99, first.price)
        assertEquals(49.99, second.price)
        assertEquals(99.99, third.price)
    }

    @Test
    fun findByIdReturnsNullForMissingProduct() {
        assertNull(catalog.findById(0))
        assertNull(catalog.findById(4))
        assertNull(catalog.findById(-1))
    }
}

class PaymentServiceTest {

    private val paymentService = PaymentService()

    @Test
    fun processPaymentReturnsOkStatus() {
        val response = paymentService.processPayment(PaymentRequest(total = 29.99))
        assertEquals("ok", response.status)
    }

    @Test
    fun processPaymentWorksForDifferentTotals() {
        val small = paymentService.processPayment(PaymentRequest(total = 1.0))
        val medium = paymentService.processPayment(PaymentRequest(total = 79.98))
        val large = paymentService.processPayment(PaymentRequest(total = 999.99))

        assertEquals("ok", small.status)
        assertEquals("ok", medium.status)
        assertEquals("ok", large.status)
    }
}
