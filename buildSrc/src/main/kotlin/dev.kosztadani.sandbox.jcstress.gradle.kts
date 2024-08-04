import java.util.stream.Collectors

plugins {
    java apply false
    distribution apply false
}

val jcstressSourceSetName = "jcstress"

val jcstressSourceSet: NamedDomainObjectProvider<SourceSet> = sourceSets.register(jcstressSourceSetName)

val jcstressImplementation: Configuration by configurations.named(jcstressSourceSet.get().implementationConfigurationName) {
    extendsFrom(configurations.implementation.get())
}

val jcstressAnnotationProcessor: Configuration by configurations.named(jcstressSourceSet.get().annotationProcessorConfigurationName) {
    extendsFrom(configurations.annotationProcessor.get())
}

val jcstressRuntimeClasspath: Configuration by configurations.named(jcstressSourceSet.get().runtimeClasspathConfigurationName) {
    extendsFrom(configurations.runtimeClasspath.get())
}

dependencies {
    jcstressImplementation(project);
    versionCatalogs.named("libs").findLibrary("jcstress").ifPresentOrElse(
        {
            jcstressImplementation(it)
            jcstressAnnotationProcessor(it)
        },
        {
            throw AssertionError("Library 'jcstress' not found in libs.versions.toml")
        }
    )
}

val jcstressJarTask = tasks.register<Jar>("jcstressJar") {
    group = "build"
    archiveFileName.set("${project.name}-jcstress.jar")
    from(jcstressSourceSet.map(SourceSet::getOutput))
    manifest {
        attributes(
            "Main-Class" to "org.openjdk.jcstress.Main",
            "Class-Path" to
                    jcstressRuntimeClasspath.files.stream().map {
                        "lib/" + it.name
                    }.collect(Collectors.joining(" "))
        )
    }
}

val copyJcstress = project.copySpec {
    from(jcstressRuntimeClasspath) {
        into("lib")
    }
    from(jcstressJarTask.map(Task::getOutputs))
}

distributions.register("jcstress") {
    contents {
        with(copyJcstress)
    }
}
