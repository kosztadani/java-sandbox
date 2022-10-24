import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    java
    checkstyle
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

dependencies {
    val junitVersion = "5.9.1"
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = junitVersion)
    testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = junitVersion)

    val mockitoVersion = "4.8.0"
    testImplementation(group = "org.mockito", name = "mockito-core", version = mockitoVersion)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events(PASSED, SKIPPED, FAILED)
    }
}
