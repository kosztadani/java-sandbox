package dev.kosztadani.sandbox

import java.util.stream.Collectors

plugins {
    java apply false
    distribution apply false
}

val jmhSourceSetName = "jmh"

val jmhSourceSet: NamedDomainObjectProvider<SourceSet> = sourceSets.register(jmhSourceSetName);

val jmhImplementation: Configuration by configurations.named(jmhSourceSet.get().implementationConfigurationName) {
    extendsFrom(configurations.implementation.get())
}

val jmhAnnotationProcessor: Configuration by configurations.named(jmhSourceSet.get().annotationProcessorConfigurationName) {
    extendsFrom(configurations.annotationProcessor.get())
}

val jmhRuntimeClasspath: Configuration by configurations.named(jmhSourceSet.get().runtimeClasspathConfigurationName) {
    extendsFrom(configurations.runtimeClasspath.get())
}

dependencies {
    jmhImplementation(project)
    versionCatalogs.named("libs").findLibrary("jmh-core").ifPresentOrElse(
        {
            jmhImplementation(it)
        },
        {
            throw AssertionError("Library 'jmh' not found in libs.versions.toml")
        }
    )
    versionCatalogs.named("libs").findLibrary("jmh-annotation-processor").ifPresentOrElse(
        {
            jmhAnnotationProcessor(it)
        },
        {
            throw AssertionError("Library 'jmh-annotation-processor' not found in libs.versions.toml")
        }
    )
}

val jmhJarTask = tasks.register<Jar>("jmhJar") {
    group = "build"
    archiveFileName.set("${project.name}-jmh.jar")
    from(jmhSourceSet.map(SourceSet::getOutput))
    manifest {
        attributes(
            "Main-Class" to "org.openjdk.jmh.Main",
            "Class-Path" to
                    jmhRuntimeClasspath.files.stream().map {
                        "lib/" + it.name
                    }.collect(Collectors.joining(" "))
        )
    }
}

val copyJmh = project.copySpec {
    from(jmhRuntimeClasspath) {
        into("lib")
    }
    from(jmhJarTask.map(Task::getOutputs))
}

distributions.register("jmh") {
    contents {
        with(copyJmh)
    }
}
