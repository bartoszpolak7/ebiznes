plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
    kotlin("plugin.jpa") version "2.4.0"
}

group = "io.github.siemamen7"
version = "0.0.1-SNAPSHOT"
description = "backend"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-resttestclient")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-restclient")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // integracja json
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // do seedowania bazy danych na początku działania
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // sterownik dla bazy danych
    runtimeOnly("org.postgresql:postgresql")
    // do haszowania hasła, ale niestety wymaga też napisania nowego pliku
    implementation("org.springframework.boot:spring-boot-starter-security")

    // OAuth2 i sprawdzanie tokenów
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

springBoot {
    mainClass.set("io.github.siemamen7.backend.ServerKt")
}
