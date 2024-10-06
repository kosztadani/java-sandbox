plugins {
    id("dev.kosztadani.sandbox.application")
}

val applicationMainClass = "dev.kosztadani.sandbox.tcp.server.MathServerMain"

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

dependencies {
    implementation(project(":tcp-example:common"))
}
