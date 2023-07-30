import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    java
    distribution
    checkstyle
    id("dev.kosztadani.sandbox.jcstress")
    id("dev.kosztadani.sandbox.jmh")
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
}

checkstyle {
    sourceSets = listOf(
        project.sourceSets.main.get(),
        project.sourceSets.test.get()
    )
}

dependencies {
    val junitJupiterVersion = "5.10.0"
    val junitPlatformVersion = "1.10.0"
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = junitJupiterVersion)
    testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = junitJupiterVersion)
    testRuntimeOnly(group = "org.junit.platform", name = "junit-platform-launcher", version = junitPlatformVersion)

    val mockitoVersion = "5.4.0"
    testImplementation(group = "org.mockito", name = "mockito-core", version = mockitoVersion)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events(PASSED, SKIPPED, FAILED)
    }
}
