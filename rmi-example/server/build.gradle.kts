plugins {
    id("dev.kosztadani.sandbox.application")
}

val applicationMainClass = "dev.kosztadani.sandbox.rmi.server.ServerMain"

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
    implementation(project(":rmi-example:common"))
}
