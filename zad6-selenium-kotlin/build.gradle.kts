import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.21"
}

group = "io.github.siemamen7"
version = "0.0.1"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

dependencies {
    // Selenium E2E
    implementation("org.seleniumhq.selenium:selenium-java:4.44.0")

    // BrowserStack Local tunneling for localhost:// apps
    implementation("com.browserstack:browserstack-local-java:1.1.6")

    // Unit test framework for Kotlin/JUnit5
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf("-Xjsr305=strict")
    }
}

