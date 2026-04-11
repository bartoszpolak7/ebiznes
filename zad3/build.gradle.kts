val kotlin_version: String by project
val logback_version: String by project
val jda_version: String by project
val ktor_version: String by project

plugins {
    kotlin("jvm") version "2.3.20"
    id("io.ktor.plugin") version "3.4.2"
    kotlin("plugin.serialization") version "2.3.20"
    id("application")
}

group = "io.github.siemamen7"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvmToolchain(25)
}

dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")

    // Ktor Client dependencies
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")

    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("net.dv8tion:JDA:$jda_version")
}
