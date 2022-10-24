import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    java
    checkstyle
    id("com.github.johnrengelman.shadow")
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

val mainSourceSetName = "main"
val testSourceSetName = "test"
val jcstressSourceSetName = "jcstress"
val jmhSourceSetName = "jmh"

checkstyle {
    sourceSets = listOf(
        project.sourceSets[mainSourceSetName],
        project.sourceSets[testSourceSetName]
    )
}

sourceSets {
    register(jcstressSourceSetName);
    register(jmhSourceSetName);
}

val jcstressImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

val jcstressAnnotationProcessor: Configuration by configurations.getting {
    extendsFrom(configurations.annotationProcessor.get())
}

val jmhImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

val jmhAnnotationProcessor: Configuration by configurations.getting {
    extendsFrom(configurations.annotationProcessor.get())
}

dependencies {
    val junitVersion = "5.9.1"
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = junitVersion)
    testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = junitVersion)

    val mockitoVersion = "4.8.0"
    testImplementation(group = "org.mockito", name = "mockito-core", version = mockitoVersion)

    val jcstressVersion = "0.15"
    jcstressImplementation(project);
    jcstressImplementation(group = "org.openjdk.jcstress", name = "jcstress-core", version = jcstressVersion)
    jcstressAnnotationProcessor(group = "org.openjdk.jcstress", name = "jcstress-core", version = jcstressVersion)

    val jmhVersion = "1.34"
    jmhImplementation(project)
    jmhImplementation(group = "org.openjdk.jmh", name = "jmh-core", version = jmhVersion)
    jmhAnnotationProcessor(group = "org.openjdk.jmh", name = "jmh-generator-annprocess", version = jmhVersion)
}

tasks.register<ShadowJar>("jcstressJar") {
    archiveFileName.set("jcstress.jar")
    from(sourceSets[jcstressSourceSetName].output)
    configurations += project.configurations["jcstressRuntimeClasspath"]
    manifest {
        attributes("Main-Class" to "org.openjdk.jcstress.Main")
    }
}

tasks.register<ShadowJar>("jmhJar") {
    archiveFileName.set("jmh.jar")
    from(sourceSets[jmhSourceSetName].output)
    configurations += project.configurations["jmhRuntimeClasspath"]
    manifest {
        attributes("Main-Class" to "org.openjdk.jmh.Main")
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events(PASSED, SKIPPED, FAILED)
    }
}
