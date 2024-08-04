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
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
    withJavadocJar()
}

checkstyle {
    sourceSets = listOf(
        project.sourceSets.main.get(),
        project.sourceSets.test.get()
    )
}

dependencies {
    versionCatalogs.named("libs").findBundle("testLibraries").ifPresentOrElse(
        {
            testImplementation(it)
        },
        {
            throw AssertionError("Bundle 'testLibraries' not found in libs.versions.toml")
        }
    )
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events(PASSED, SKIPPED, FAILED)
    }
}
