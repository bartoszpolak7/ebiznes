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
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    // Selenium E2E
    implementation("org.seleniumhq.selenium:selenium-java:4.44.0")

    // BrowserStack Local tunneling for localhost:// apps
    implementation("com.browserstack:browserstack-local-java:1.1.6")

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("com.browserstack:browserstack-java-sdk:latest.release")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    val sdkJarFile by lazy {
        configurations.detachedConfiguration(
            dependencies.create("com.browserstack:browserstack-java-sdk:latest.release")
        ).singleFile
    }
    doFirst {
        println("BrowserStack SDK jar: $sdkJarFile")
    }
    jvmArgs("-javaagent:$sdkJarFile")
}