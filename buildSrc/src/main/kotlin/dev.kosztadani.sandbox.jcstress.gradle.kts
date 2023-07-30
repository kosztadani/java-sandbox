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
    val jcstressVersion = "0.16"
    jcstressImplementation(project);
    jcstressImplementation(group = "org.openjdk.jcstress", name = "jcstress-core", version = jcstressVersion)
    jcstressAnnotationProcessor(group = "org.openjdk.jcstress", name = "jcstress-core", version = jcstressVersion)
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
