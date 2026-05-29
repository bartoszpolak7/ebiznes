package io.github.siemamen7.zad6

import com.browserstack.local.Local
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.HttpURLConnection
import java.net.URLEncoder
import java.net.URL
import java.time.Duration
import java.util.UUID

abstract class BrowserStackE2EBase {
    protected lateinit var driver: WebDriver

    companion object {
        private var local: Local? = null
        private var localIdentifier: String = UUID.randomUUID().toString()

        private fun requireBrowserStackEnv() {
            val username = System.getenv("BROWSERSTACK_USERNAME")
            val accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY")
            Assumptions.assumeTrue(!username.isNullOrBlank(), "Missing BROWSERSTACK_USERNAME")
            Assumptions.assumeTrue(!accessKey.isNullOrBlank(), "Missing BROWSERSTACK_ACCESS_KEY")
        }

        @JvmStatic
        @BeforeAll
        fun startBrowserStackLocal() {
            val accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY")
            if (accessKey.isNullOrBlank()) return

            requireBrowserStackEnv()

            localIdentifier = System.getenv("BROWSERSTACK_LOCAL_IDENTIFIER") ?: localIdentifier
            val forceLocal = System.getenv("BROWSERSTACK_LOCAL_FORCELOCAL") ?: "true"
            local = Local().also { bsLocal ->
                val args = hashMapOf(
                    "key" to accessKey,
                    "forcelocal" to forceLocal,
                    "localIdentifier" to localIdentifier
                )
                bsLocal.start(args)

                val deadline = System.currentTimeMillis() + Duration.ofSeconds(60).toMillis()
                while (!bsLocal.isRunning() && System.currentTimeMillis() < deadline) {
                    Thread.sleep(1000)
                }
                check(bsLocal.isRunning()) { "BrowserStack Local didn't start in time." }
            }
        }

        @JvmStatic
        @AfterAll
        fun stopBrowserStackLocal() {
            local?.stop()
        }
    }

    @BeforeEach
    fun setUp(testInfo: TestInfo) {
        requireBrowserStackEnv()
        waitForBackendReady()

        val username = System.getenv("BROWSERSTACK_USERNAME")!!
        val accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY")!!

        val buildName = System.getenv("BROWSERSTACK_BUILD_NAME")
            ?: "zad6-e2e-${java.time.LocalDate.now()}"

        val sessionName = "[Zad6] ${testInfo.displayName}"

        val remoteUrl =
            "https://${URLEncoder.encode(username, "UTF-8")}:${URLEncoder.encode(accessKey, "UTF-8")}@hub-cloud.browserstack.com/wd/hub"

        val options = ChromeOptions()
        options.setCapability("browserName", "Chrome")
        options.setCapability("browserVersion", "latest")
        options.setCapability("sessionName", sessionName)
        options.setCapability("buildName", buildName)

        val bstackOptions = hashMapOf<String, Any>(
            "os" to "Windows",
            "osVersion" to "10",
            "local" to "true",
            "localIdentifier" to localIdentifier
        )
        options.setCapability("bstack:options", bstackOptions)

        driver = RemoteWebDriver(URL(remoteUrl), options)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0))
    }

    @AfterEach
    fun tearDown() {
        if (this::driver.isInitialized) {
            driver.quit()
        }
    }

    protected fun currentUrl(): String = requireNotNull(driver.currentUrl) { "Browser did not report current URL" }

    protected fun pageSource(): String = requireNotNull(driver.pageSource) { "Browser did not report page source" }

    protected fun waitForTextVisible(text: String, timeoutSeconds: Long = 10) {
        val wait = WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[normalize-space()=${xpathLiteral(text)}]")
            )
        )
    }

    protected fun waitForUrlContains(fragment: String, timeoutSeconds: Long = 10) {
        val wait = WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
        wait.until(ExpectedConditions.urlContains(fragment))
    }

    private fun xpathLiteral(value: String): String =
        if (!value.contains("'")) "'$value'" else "\"$value\""

    private fun waitForBackendReady() {
        val backendHealthUrl =
            System.getenv("ZAD6_BACKEND_HEALTH_URL")
                ?: System.getenv("ZAD5_BACKEND_HEALTH_URL")
                ?: "http://localhost:8080/products"
        val deadline = System.currentTimeMillis() + Duration.ofSeconds(30).toMillis()

        while (System.currentTimeMillis() < deadline) {
            try {
                val conn = URL(backendHealthUrl).openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.connectTimeout = 2000
                conn.readTimeout = 2000
                if (conn.responseCode in 200..299) return
            } catch (_: Exception) {
                // retry
            }
            Thread.sleep(500)
        }
        throw IllegalStateException("Backend not ready: $backendHealthUrl")
    }

    private fun requireBrowserStackEnv() {
        val username = System.getenv("BROWSERSTACK_USERNAME")
        val accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY")
        Assumptions.assumeTrue(!username.isNullOrBlank(), "Missing BROWSERSTACK_USERNAME")
        Assumptions.assumeTrue(!accessKey.isNullOrBlank(), "Missing BROWSERSTACK_ACCESS_KEY")
    }
}

