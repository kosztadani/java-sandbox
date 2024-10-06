plugins {
    id("dev.kosztadani.sandbox.java-application-conventions")
}

dependencies {
    implementation(project(":tcp-example:common"))
    implementation(project(":tcp-example:server"))
    implementation(project(":tcp-example:client"))
}
