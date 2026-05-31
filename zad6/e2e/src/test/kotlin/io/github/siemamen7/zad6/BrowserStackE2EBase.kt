package io.github.siemamen7.zad6


import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import java.net.URL

abstract class BrowserStackE2EBase {

    // 1. Keep the driver at the instance level (NOT in the companion object)
    protected lateinit var driver: WebDriver

    protected fun currentUrl(): String = requireNotNull(driver.currentUrl) { "Browser did not report current URL" }
    protected fun pageSource(): String = requireNotNull(driver.pageSource) { "Browser did not report page source" }

    // 2. Use @BeforeEach so every single test method gets a fresh browser window
    @BeforeEach
    fun setUp() {
        val options = ChromeOptions()
        // Points to your Selenium Grid or BrowserStack hub
        driver = RemoteWebDriver(URL("http://localhost:4444/wd/hub"), options)
    }

    // 3. Use @AfterEach to reliably close the browser window after the test finishes
    @AfterEach
    fun tearDown() {
        if (this::driver.isInitialized) {
            driver.quit()
        }
    }
}
