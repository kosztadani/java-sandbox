plugins {
    id("dev.kosztadani.sandbox.java-application-conventions")
}

dependencies {
}

val applicationMainClass = "dev.kosztadani.sandbox.app.App"

application {
    mainClass.set(applicationMainClass)
}

tasks.jar {
    manifest {
        attributes(
            mapOf("Main-Class" to applicationMainClass)
        )
    }
}