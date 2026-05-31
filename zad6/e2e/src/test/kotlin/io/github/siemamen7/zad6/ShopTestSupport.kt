package io.github.siemamen7.zad6

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class ShopTestSupport(private val driver: WebDriver) {
    val frontendUrl: String =
        System.getenv("ZAD6_FRONTEND_URL")
            ?: System.getenv("ZAD6_FRONTEND_URL")
            ?: "http://localhost:5173"

    private fun wait(): WebDriverWait = WebDriverWait(driver, Duration.ofSeconds(15))

    fun openProducts() {
        driver.get(frontendUrl)
        waitForHeading("Products")
    }

    fun openCart() {
        driver.get("$frontendUrl/cart")
        waitForHeading("Cart")
    }

    fun openPayment() {
        driver.get("$frontendUrl/payment")
        waitForHeading("Payment")
    }

    fun waitForHeading(text: String) {
        wait().until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[normalize-space()='$text']")
            )
        )
    }

    fun productCards(): List<WebElement> =
        driver.findElements(By.xpath("//h2[normalize-space()='Products']/following-sibling::div[.//button[contains(.,'Add to cart')]]"))

    fun addToCartButtons(): List<WebElement> =
        driver.findElements(By.xpath("//button[contains(normalize-space(),'Add to cart')]"))

    fun addProductToCart(productName: String) {
        val button = wait().until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//h3[normalize-space()='$productName']/parent::div//button[contains(.,'Add to cart')]")
            )
        )
        button.click()
    }

    fun clickGoToCart() {
        wait().until(ExpectedConditions.elementToBeClickable(By.linkText("Go to cart"))).click()
        waitForHeading("Cart")
    }

    fun clickPaymentLink() {
        wait().until(ExpectedConditions.elementToBeClickable(By.linkText("Payment"))).click()
        waitForHeading("Payment")
    }

    fun clickBackToProducts() {
        wait().until(ExpectedConditions.elementToBeClickable(By.linkText("Back to products"))).click()
        waitForHeading("Products")
    }

    fun cartTotalText(): String {
        val el = wait().until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[normalize-space()='Cart']/following::div[contains(normalize-space(),'Total:')][1]")
            )
        )
        return el.text
    }

    fun paymentTotalText(): String {
        val el = wait().until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[normalize-space()='Payment']/following::p[contains(normalize-space(),'Total:')][1]")
            )
        )
        return el.text
    }

    fun payNowButton(): WebElement =
        wait().until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(normalize-space(),'Pay now')]")
            )
        )

    fun acceptSuccessAlert() {
        val alert = wait().until(ExpectedConditions.alertIsPresent())
        alert.accept()
    }

    fun quantityForProduct(productName: String): String {
        val el = wait().until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h3[normalize-space()='$productName']/following::p[contains(normalize-space(),'Quantity:')][1]")
            )
        )
        return el.text
    }

    fun cartContainsProduct(productName: String): Boolean =
        driver.findElements(By.xpath("//h3[normalize-space()='$productName']")).isNotEmpty()
}
