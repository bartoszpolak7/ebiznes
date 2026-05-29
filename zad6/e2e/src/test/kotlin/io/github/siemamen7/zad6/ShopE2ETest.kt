package io.github.siemamen7.zad6

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ShopE2ETest : BrowserStackE2EBase() {
    private lateinit var shop: ShopTestSupport

    @BeforeEach
    fun initShop() {
        shop = ShopTestSupport(driver)
    }

    @Test
    fun test01_productsPageLoadsWithHeading() {
        shop.openProducts()
        assertTrue(currentUrl().endsWith("/") || currentUrl().endsWith(":5173"))
        assertTrue(pageSource().contains("Products"))
    }

    @Test
    fun test02_productsPageShowsThreeProducts() {
        shop.openProducts()
        assertEquals(3, shop.productCards().size)
        assertEquals(3, shop.addToCartButtons().size)
    }

    @Test
    fun test03_eachProductShowsNameAndPrice() {
        shop.openProducts()
        val expected = listOf(
            "Dziadek do orzechów" to "29.99 zł",
            "Lego Ninjago" to "49.99 zł",
            "Buty na rzepy" to "99.99 zł"
        )
        expected.forEach { (name, price) ->
            assertTrue(pageSource().contains(name))
            assertTrue(pageSource().contains(price))
        }
    }

    @Test
    fun test04_eachProductHasAddToCartButton() {
        shop.openProducts()
        shop.addToCartButtons().forEach { button ->
            assertTrue(button.isDisplayed)
            assertTrue(button.isEnabled)
        }
    }

    @Test
    fun test05_goToCartNavigatesToCartPage() {
        shop.openProducts()
        shop.clickGoToCart()
        assertTrue(currentUrl().contains("/cart"))
        assertTrue(pageSource().contains("Cart"))
    }

    @Test
    fun test06_addSingleProductShowsQuantityOne() {
        shop.openProducts()
        shop.addProductToCart("Dziadek do orzechów")
        shop.clickGoToCart()
        assertEquals("Quantity: 1", shop.quantityForProduct("Dziadek do orzechów"))
        assertTrue(shop.cartContainsProduct("Dziadek do orzechów"))
    }

    @Test
    fun test07_addSameProductTwiceShowsQuantityTwo() {
        shop.openProducts()
        shop.addProductToCart("Lego Ninjago")
        shop.addProductToCart("Lego Ninjago")
        shop.clickGoToCart()
        assertEquals("Quantity: 2", shop.quantityForProduct("Lego Ninjago"))
        assertTrue(shop.cartTotalText().contains("99.98"))
    }

    @Test
    fun test08_addTwoDifferentProductsShowsBothInCart() {
        shop.openProducts()
        shop.addProductToCart("Dziadek do orzechów")
        shop.addProductToCart("Buty na rzepy")
        shop.clickGoToCart()
        assertTrue(shop.cartContainsProduct("Dziadek do orzechów"))
        assertTrue(shop.cartContainsProduct("Buty na rzepy"))
        assertTrue(shop.cartTotalText().contains("129.98"))
    }

    @Test
    fun test09_cartPageShowsHeadingAndItems() {
        shop.openProducts()
        shop.addProductToCart("Lego Ninjago")
        shop.clickGoToCart()
        assertTrue(pageSource().contains("Cart"))
        assertTrue(shop.cartContainsProduct("Lego Ninjago"))
        assertTrue(shop.cartTotalText().contains("49.99"))
    }

    @Test
    fun test10_cartTotalMatchesAddedProducts() {
        shop.openProducts()
        shop.addProductToCart("Dziadek do orzechów")
        shop.addProductToCart("Dziadek do orzechów")
        shop.clickGoToCart()
        assertTrue(shop.cartTotalText().contains("59.98"))
        assertTrue(shop.quantityForProduct("Dziadek do orzechów").contains("2"))
    }

    @Test
    fun test11_quantityLabelContainsNumericValue() {
        shop.openProducts()
        shop.addProductToCart("Buty na rzepy")
        shop.clickGoToCart()
        val quantity = shop.quantityForProduct("Buty na rzepy")
        assertTrue(quantity.startsWith("Quantity: "))
        assertTrue(quantity.substringAfter(": ").toIntOrNull() == 1)
    }

    @Test
    fun test12_paymentLinkNavigatesToPaymentPage() {
        shop.openProducts()
        shop.addProductToCart("Lego Ninjago")
        shop.clickGoToCart()
        shop.clickPaymentLink()
        assertTrue(currentUrl().contains("/payment"))
        assertTrue(pageSource().contains("Payment"))
    }

    @Test
    fun test13_backToProductsNavigatesToHome() {
        shop.openCart()
        shop.clickBackToProducts()
        assertTrue(currentUrl().endsWith("/") || currentUrl().endsWith(":5173"))
        assertTrue(pageSource().contains("Products"))
    }

    @Test
    fun test14_emptyCartShowsZeroTotal() {
        shop.openCart()
        assertTrue(shop.cartTotalText().contains("Total: 0"))
        assertFalse(shop.cartContainsProduct("Dziadek do orzechów"))
    }

    @Test
    fun test15_cartTotalMatchesPaymentTotal() {
        shop.openProducts()
        shop.addProductToCart("Buty na rzepy")
        shop.clickGoToCart()
        val cartTotal = shop.cartTotalText()
        shop.clickPaymentLink()
        val paymentTotal = shop.paymentTotalText()
        assertTrue(cartTotal.contains("99.99"))
        assertTrue(paymentTotal.contains("99.99"))
    }

    @Test
    fun test16_payNowDisabledForEmptyCart() {
        shop.openPayment()
        assertFalse(shop.payNowButton().isEnabled)
        assertTrue(shop.paymentTotalText().contains("0.00"))
    }

    @Test
    fun test17_payNowEnabledWhenCartHasItems() {
        shop.openProducts()
        shop.addProductToCart("Lego Ninjago")
        shop.clickGoToCart()
        shop.clickPaymentLink()
        assertTrue(shop.payNowButton().isEnabled)
        assertTrue(shop.paymentTotalText().contains("49.99"))
    }

    @Test
    fun test18_payNowShowsSuccessAlert() {
        shop.openProducts()
        shop.addProductToCart("Dziadek do orzechów")
        shop.clickGoToCart()
        shop.clickPaymentLink()
        shop.payNowButton().click()
        shop.acceptSuccessAlert()
        assertTrue(pageSource().contains("Payment"))
    }

    @Test
    fun test19_cartClearedAfterSuccessfulPayment() {
        shop.openProducts()
        shop.addProductToCart("Lego Ninjago")
        shop.clickGoToCart()
        shop.clickPaymentLink()
        shop.payNowButton().click()
        shop.acceptSuccessAlert()
        shop.openCart()
        assertTrue(shop.cartTotalText().contains("Total: 0"))
        assertFalse(shop.cartContainsProduct("Lego Ninjago"))
    }

    @Test
    fun test20_fullCheckoutFlowFromProductsToPayment() {
        shop.openProducts()
        assertEquals(3, shop.productCards().size)
        shop.addProductToCart("Dziadek do orzechów")
        shop.addProductToCart("Lego Ninjago")
        shop.clickGoToCart()
        assertTrue(shop.cartTotalText().contains("79.98"))
        shop.clickPaymentLink()
        assertTrue(shop.payNowButton().isEnabled)
        shop.payNowButton().click()
        shop.acceptSuccessAlert()
        shop.openCart()
        assertTrue(shop.cartTotalText().contains("Total: 0"))
    }
}
